package com.contest.service.impl;

import com.contest.config.filesys.FileSystemEnvInfo;
import com.contest.dto.filesys.FileTextDto;
import com.contest.dto.filesys.FileUploadDto;
import com.contest.dto.filesys.FileUploadRequest;
import com.contest.dto.filesys.SimpleFileUploadDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.filesys.FileInstanceEntity;
import com.contest.entity.filesys.FileReferenceEntity;
import com.contest.entity.filesys.FileTimeoutEntity;
import com.contest.mapper.FileInstanceMapper;
import com.contest.mapper.FileReferenceMapper;
import com.contest.pojo.filesys.FileUploadSession;
import com.contest.result.ResultFlag;
import com.contest.result.ResultModel;
import com.contest.util.Md5Utils;
import com.contest.service.UploadService;
import com.contest.service.md5lock.Md5Lock;
import com.contest.util.FileUtils;
import com.contest.util.RedisUtil;
import com.contest.util.SnowMaker;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

@Service
public class UploadServiceImpl implements UploadService{

    @Resource
    private FileSystemEnvInfo fileSystemEnvInfo;

    @Resource
    private FileReferenceMapper fileReferenceMapper;

    @Resource
    private FileInstanceMapper fileInstanceMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private Md5Lock md5Lock;

    @Resource
    private FileTimeoutService fileTimeoutService;

    @Value("${filesys.download-url-prefix.inline}")
    private String inlineDownloadUrlPrefix;

    @Value("${filesys.download-url-prefix.attachment}")
    private String attachmentDownloadUrlPrefix;

    private static final SnowMaker snowMaker = new SnowMaker(1);

    @Override
    @Transactional
    public ResultModel<String> requestConcurrentUpload(FileUploadRequest fileUploadRequest) {
        // 判断文件是否存在
        File targetFile = new File(fileSystemEnvInfo.buildFilePath(fileUploadRequest.getFileMd5()));
        if(targetFile.exists()){
            FileReferenceEntity fileReferenceEntity = FileReferenceEntity
                    .builder()
                    .fileName(fileUploadRequest.getFileName())
                    .fileMd5(fileUploadRequest.getFileMd5())
                    .fileId(snowMaker.nextId())
                    .createdDate(new Date())
                    .createdBy(fileUploadRequest.getUserId())
                    .isPublic(fileUploadRequest.isPublicPerm())
                    .build();
            fileReferenceMapper.insert(fileReferenceEntity);
            fileInstanceMapper.incrReferenceNum(fileUploadRequest.getFileMd5());
            ResultModel<String> resultModel = ResultModel.buildSuccessResultModel();
            resultModel.setData(buildAttachmentFileDownloadUrl(fileReferenceEntity.getFileId().toString()));
            return resultModel;
        }
        // 创建session
        String sessionId = UUID.randomUUID().toString();
        FileUploadSession fileUploadSession = new FileUploadSession(
                sessionId,
                fileUploadRequest.getFileMd5(),
                fileUploadRequest.getFileName(),
                fileSystemEnvInfo.buildFilePath(sessionId),
                fileUploadRequest.getUserId(),
                fileUploadRequest.isPublicPerm()
        );
        setFileUploadSessionIntoCache(fileUploadSession);
        // 返回SessionID
        ResultModel<String> resultModel = ResultModel.buildContinueResultModel();
        resultModel.setData(sessionId);
        return resultModel;
    }

    @Override
    @Transactional
    public ResultModel<String> uploadFilePiece(FileUploadDto fileUploadDto) {
        FileUploadSession fileUploadSession = getFileUploadSessionFromCache(fileUploadDto.getSessionId());
        // 保存文件分片
        try {
            saveFile(fileUploadSession.getFilePath(),fileUploadDto.getPieceInputStream());
        } catch (IOException e) {
            FileUtils.deleteFile(fileUploadSession.getFilePath());
            throw new RuntimeException(e);
        }
        // 是否为最后一片
        if(!fileUploadDto.isLast()){
            return ResultModel.buildContinueResultModel();
        }
        // 原子操作： 删除Session、检查是否存在、插入数据库
        deleteFileUploadSessionFromCache(fileUploadSession.getSessionId());
        try{
            lockMd5(fileUploadSession.getFileMd5());
            File targetFile = new File(fileSystemEnvInfo.buildFilePath(fileUploadSession.getFileMd5()));
            File sessionFile = new File(fileUploadSession.getFilePath());
            if(!targetFile.exists()){
                boolean renameResult = sessionFile.renameTo(targetFile);
                if(!renameResult){
                    throw new IOException("文件重命名失败！");
                }
                FileInstanceEntity fileInstanceEntity = FileInstanceEntity
                        .builder()
                        .fileMd5(fileUploadSession.getFileMd5())
                        .filePath(targetFile.getPath())
                        .fileSize(targetFile.length())
                        .referenceNum(1)
                        .createdBy(fileUploadSession.getUserId())
                        .createdDate(new Date())
                        .build();
                saveFileInstance(fileInstanceEntity);
            }else{
                fileInstanceMapper.incrReferenceNum(fileUploadSession.getFileMd5());
                FileUtils.deleteFile(sessionFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            unlockMd5(fileUploadSession.getFileMd5());
        }
        FileReferenceEntity fileReferenceEntity = FileReferenceEntity
                .builder()
                .fileId(snowMaker.nextId())
                .fileMd5(fileUploadSession.getFileMd5())
                .fileName(fileUploadSession.getFileName())
                .createdDate(new Date())
                .createdBy(fileUploadSession.getUserId())
                .isPublic(fileUploadSession.isPublic())
                .build();
        saveFileReference(fileReferenceEntity);
        ResultModel<String> resultModel = ResultModel.buildSuccessResultModel();
        resultModel.setData(buildAttachmentFileDownloadUrl(fileReferenceEntity.getFileId().toString()));
        return resultModel;
    }

    /**
     * 上传简单文件
     * */
    @SneakyThrows
    @Transactional
    @Override
    public ResultModel<String> uploadSimpleFile(UserDto userDto, SimpleFileUploadDto simpleFileUploadDto) {
        byte[] fileBytes = simpleFileUploadDto.getFileBytes();
        String sessionId = UUID.randomUUID().toString();
        File file = new File(fileSystemEnvInfo.buildFilePath(sessionId));
        BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
        out.write(fileBytes);
        out.flush();
        out.close();
        InputStream in = new BufferedInputStream(Files.newInputStream(file.toPath()));
        String fileMd5 = Md5Utils.getFileMd5(in);
        in.close();
        File targetFile = new File(fileSystemEnvInfo.buildFilePath(fileMd5));
        Long fileId = snowMaker.nextId();
        FileReferenceEntity fileReferenceEntity = FileReferenceEntity
                .builder()
                .fileName(simpleFileUploadDto.getFileName())
                .fileMd5(fileMd5)
                .fileId(fileId)
                .isPublic(simpleFileUploadDto.isPublicPerm())
                .createdBy(userDto.getUserId())
                .createdDate(new Date())
                .build();
        saveFileReference(fileReferenceEntity);
        boolean refIncr = true;
        if(!targetFile.exists()){
            try{
                lockMd5(fileMd5);
                if(!targetFile.exists()){
                    FileInstanceEntity fileInstanceEntity = FileInstanceEntity
                            .builder()
                            .filePath(targetFile.getPath())
                            .fileSize(file.length())
                            .referenceNum(1)
                            .fileMd5(fileMd5)
                            .createdBy(userDto.getUserId())
                            .createdDate(new Date())
                            .build();
                    saveFileInstance(fileInstanceEntity);
                    file.renameTo(targetFile);
                    refIncr = false;
                }
            }finally {
                unlockMd5(fileMd5);
            }
        }
        if(refIncr){
            fileInstanceMapper.incrReferenceNum(fileMd5);
        }
        if(simpleFileUploadDto.isTimeLimit()){
            fileTimeoutService.addTimeoutFile(
                    FileTimeoutEntity
                            .builder()
                            .fileId(fileId)
                            .timeout(System.currentTimeMillis() + 1000*3600*24)
                            .createdDate(new Date())
                            .build()
            );
        }
        ResultModel<String> resultModel = new ResultModel<>();
        resultModel.setResultCode(ResultFlag.SUCCESS.code);
        resultModel.setResultFlag(ResultFlag.SUCCESS);
        resultModel.setData(buildInlineFileDownloadUrl(fileId));
        return resultModel;
    }

    /**
     * 上传文本
     * */
    @Override
    @Transactional
    public ResultModel<String> uploadText(FileTextDto fileTextDto, UserDto userDto) {
        String fileMd5 = Md5Utils.getTextMd5(fileTextDto.getText());
        FileInstanceEntity fileInstanceInDb = fileInstanceMapper.selectById(fileMd5);
        long fileId = snowMaker.nextId();
        if(fileTextDto.isTimeLimit()){
            fileTimeoutService.addTimeoutFile(
                    FileTimeoutEntity
                            .builder()
                            .fileId(fileId)
                            .timeout(System.currentTimeMillis() + 1000*3600*24)
                            .createdDate(new Date())
                            .build()
            );
        }
        FileReferenceEntity fileReferenceEntity = FileReferenceEntity
                .builder()
                .fileMd5(fileMd5)
                .fileId(fileId)
                .isPublic(fileTextDto.isPublicPerm())
                .fileName(fileTextDto.getFileName())
                .createdBy(userDto.getUserId())
                .createdDate(new Date())
                .updatedBy(userDto.getUserId())
                .updatedDate(new Date())
                .build();
        if(fileInstanceInDb != null){
            fileInstanceMapper.incrReferenceNum(fileMd5);
            fileReferenceMapper.insert(fileReferenceEntity);
            return ResultModel.buildSuccessResultModel(null,buildInlineFileDownloadUrl(fileId));
        }
        lockMd5(fileMd5);
        try{
            fileInstanceInDb = fileInstanceMapper.selectById(fileMd5);
            fileReferenceMapper.insert(fileReferenceEntity);
            if(fileInstanceInDb != null){
                fileInstanceMapper.incrReferenceNum(fileMd5);
                return ResultModel.buildSuccessResultModel(null,buildInlineFileDownloadUrl(fileId));
            }
            String filePath = fileSystemEnvInfo.buildFilePath(fileReferenceEntity.getFileMd5());
            File targetFile = new File(filePath);
            BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
            writer.write(fileTextDto.getText());
            writer.close();
            FileInstanceEntity fileInstanceEntity = FileInstanceEntity
                    .builder()
                    .fileMd5(fileMd5)
                    .fileSize(targetFile.length())
                    .filePath(targetFile.getPath())
                    .referenceNum(0)
                    .createdBy(userDto.getUserId())
                    .createdDate(new Date())
                    .updatedBy(userDto.getUserId())
                    .updatedDate(new Date())
                    .build();
            fileInstanceMapper.insert(fileInstanceEntity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            unlockMd5(fileMd5);
        }
        return ResultModel.buildSuccessResultModel(null,buildInlineFileDownloadUrl(fileId));
    }

    /**
     * 将session指定的文件保存文件到磁盘
     * */
    public void saveFile(String filePath, InputStream in) throws IOException {
        File file = new File(filePath);
        FileUtils.writeFileByAppend(in,file);
    }

    public void saveFileInstance(FileInstanceEntity fileInstanceEntity){
        Optional<FileInstanceEntity> fileInstanceEntityOptional = Optional.ofNullable(fileInstanceMapper.selectById(fileInstanceEntity.getFileMd5()));
        fileInstanceEntityOptional.ifPresent(fileInstance -> {
            fileInstance.incrementReferenceNum();
            fileInstance.setUpdatedDate(new Date());
            fileInstance.setUpdatedBy("system");
            fileInstanceMapper.updateById(fileInstance);
        });
        if(!fileInstanceEntityOptional.isPresent()){
            fileInstanceMapper.insert(fileInstanceEntity);
        }
    }

    public void saveFileReference(FileReferenceEntity fileReferenceEntity){
        fileReferenceMapper.insert(fileReferenceEntity);
    }

    public String buildInlineFileDownloadUrl(Long fileId){
        return inlineDownloadUrlPrefix.concat(String.valueOf(fileId));
    }

    public String buildAttachmentFileDownloadUrl(String fileId){
        return attachmentDownloadUrlPrefix.concat(fileId);
    }

    public void lockMd5(String fileMd5){
        md5Lock.lock(fileMd5);
    }

    public void unlockMd5(String fileMd5){
        md5Lock.unlock(fileMd5);
    }

    public FileUploadSession getFileUploadSessionFromCache(String sessionId){
        Object value = redisUtil.getForObject("FILE_UPLOAD_SESSION_".concat(sessionId));
        if(value == null){
            return null;
        }
        return (FileUploadSession) value;
    }

    public void setFileUploadSessionIntoCache(FileUploadSession fileUploadSession){
        redisUtil.set("FILE_UPLOAD_SESSION_".concat(fileUploadSession.getSessionId()), fileUploadSession);
    }

    public void deleteFileUploadSessionFromCache(String sessionId){
        redisUtil.delete("FILE_UPLOAD_SESSION_".concat(sessionId));
    }

}
