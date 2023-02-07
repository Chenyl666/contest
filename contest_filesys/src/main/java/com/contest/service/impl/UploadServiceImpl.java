package com.contest.service.impl;

import com.contest.config.filesys.FileSystemEnvInfo;
import com.contest.dto.filesys.FileUploadDto;
import com.contest.dto.filesys.FileUploadRequest;
import com.contest.entity.filesys.FileInstanceEntity;
import com.contest.entity.filesys.FileReferenceEntity;
import com.contest.mapper.FileInstanceMapper;
import com.contest.mapper.FileReferenceMapper;
import com.contest.pojo.filesys.FileUploadSession;
import com.contest.result.ResultModel;
import com.contest.service.UploadService;
import com.contest.service.md5lock.Md5Lock;
import com.contest.util.FileUtils;
import com.contest.util.RedisUtil;
import com.contest.util.SnowMaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    public static final Map<String, Lock> md5LocksMap = new ConcurrentHashMap<>();

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
            return ResultModel.buildSuccessResultModel("上传成功！");
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
        ResultModel<String> resultModel = ResultModel.buildContinueResultModel("请求成功！");
        resultModel.setData(sessionId);
        return resultModel;
    }

    @Override
    @Transactional
    public ResultModel<String> uploadFilePiece(FileUploadDto fileUploadDto) {
        FileUploadSession fileUploadSession = getFileUploadSessionFromCache(fileUploadDto.getSessionId());
        // 保存文件分片
        try {
            saveFile(fileUploadSession,fileUploadDto.getPieceInputStream());
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            unlockMd5(fileUploadSession.getFileMd5());
        }
        saveFileReference(
                FileReferenceEntity
                        .builder()
                        .fileId(snowMaker.nextId())
                        .fileMd5(fileUploadSession.getFileMd5())
                        .fileName(fileUploadSession.getFileName())
                        .createdDate(new Date())
                        .createdBy(fileUploadSession.getUserId())
                        .isPublic(fileUploadSession.isPublic())
                        .build()
        );
        return ResultModel.buildSuccessResultModel("上传成功！");
    }

    public void saveFile(FileUploadSession fileUploadSession, InputStream in) throws IOException {
        File file = new File(fileUploadSession.getFilePath());
        File parentFile = file.getParentFile();
        boolean parentFileExists = parentFile.exists() || parentFile.mkdirs();
        if(!parentFileExists){
            throw new IOException("目录创建失败！");
        }
        boolean fileExists = file.exists() || file.createNewFile();
        if(!fileExists){
            throw new IOException("文件创建失败！");
        }
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file,true));
        byte[] buf = new byte[1024];
        int byteCnt;
        while((byteCnt = in.read(buf)) != -1){
            out.write(buf,0,byteCnt);
            out.flush();
        }
        out.close();
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
