package com.contest.service.upload.impl;

import com.contest.thread.ThreadPoolFactory;
import com.contest.cache.FileUploadSessionList;
import com.contest.config.filesys.FileSystemEnvInfo;
import com.contest.dto.filesys.FileDto;
import com.contest.entity.filesys.FileReferenceEntity;
import com.contest.mapper.FileReferenceMapper;
import com.contest.service.upload.fus.FileUploadSession;
import com.contest.util.FileUtils;
import com.contest.util.RedisUtil;
import com.contest.dto.filesys.FileUploadDto;
import com.contest.entity.filesys.FileInstanceEntity;
import com.contest.mapper.FileInstanceMapper;
import com.contest.result.ResultModel;
import com.contest.service.upload.UploadService;
import com.contest.dto.filesys.FileUploadRequest;
import com.contest.service.upload.fus.FileUploadSessionFactory;
import com.contest.util.SnowMaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UploadServiceImpl implements UploadService {

    @Value("${filesys.path}")
    private String filePath;

    @Resource
    private FileInstanceMapper fileInstanceMapper;

    @Resource
    private FileReferenceMapper fileReferenceMapper;

    @Resource
    private FileUploadSessionFactory fileUploadSessionFactory;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private FileSystemEnvInfo fileSystemEnvInfo;

    @Resource
    private ThreadPoolFactory threadPoolFactory;

    public static SnowMaker snowMaker = new SnowMaker(1);

    public static final Map<String, Lock> md5LocksMap = new ConcurrentHashMap<>();


    /**
     * 请求上传文件
     * */
    @Override
    public ResultModel<String> requestConcurrentUpload(FileUploadRequest fileUploadRequest) {
        String fileMd5 = fileUploadRequest.getFileMd5();
        // 检查文件是否存在
        Optional<FileInstanceEntity> fileInstanceEntityOptional = Optional.ofNullable(fileInstanceMapper.selectById(fileMd5));
        if(fileInstanceEntityOptional.isPresent()){
            fileReferenceMapper.insert(buildFileReferenceEntity(fileUploadRequest));
            return ResultModel.buildSuccessResultModel("文件上传成功！");
        }
        // 创建文件上传session
        FileUploadSession fileUploadSession = fileUploadSessionFactory.createFileUploadSession(fileUploadRequest);
        // 创建session
        Lock md5Lock = Optional.ofNullable(
                md5LocksMap.putIfAbsent(fileMd5, new ReentrantLock())
        ).orElse(
                md5LocksMap.get(fileMd5)
        );
        md5Lock.lock();
        try{
            Optional<FileUploadSessionList> fileUploadSessionListOptional = Optional.ofNullable(getFileUploadSessionIdListFromCache(fileMd5));
            FileUploadSessionList fileUploadSessionList = fileUploadSessionListOptional.orElse(
                    FileUploadSessionList.builder()
                            .fileMd5(fileMd5)
                            .sessionIds(new ArrayList<String>())
                            .isComplete(false)
                            .build()
            );
            fileUploadSessionList.getSessionIds().add(fileUploadSession.getSessionId());
            setFileUploadSessionIdListIntoCache(fileUploadSessionList);
            setFileUploadSessionIntoCache(fileUploadSession);
        }finally {
            md5Lock.unlock();
        }

        // 返回sessionID
        ResultModel<String> resultModel = ResultModel.buildContinueResultModel();
        resultModel.setData(fileUploadSession.getSessionId());
        return resultModel;
    }

    /**
     * 文件分片断点续传
     * */
    @Override
    public ResultModel<String> uploadFilePiece(FileUploadDto fileUploadDto) {
        FileUploadSession fileUploadSession = getFileUploadSessionFromCache(fileUploadDto.getSessionId());
        String fileMd5 = fileUploadSession.getFileMd5();
        FileUploadSessionList fileUploadSessionList = getFileUploadSessionIdListFromCache(fileMd5);
        File realFile = new File(fileUploadSession.getRealFilePath());
        File swapFile = new File(fileUploadSession.getSwapFilePath());

        // 判断是否已经有别的session上传完成
        assert fileUploadSessionList != null;
        if(fileUploadSessionList.isComplete()){
            saveFileData(buildFileDto(fileUploadSession));
            FileUtils.deleteFile(swapFile);
            return ResultModel.buildSuccessResultModel("文件上传完成！");
        }
        // 保存文件分片
        try {
            saveFilePiece(fileUploadDto.getPieceInputStream(), swapFile);
        } catch (IOException e) {
            e.printStackTrace();
            // todo 清除缓存
            swapFile.delete();
            return ResultModel.buildFailResultModel("文件上传中断!");
        }
        // 如果不是最后一个分片则向客户端要求继续上传
        if(!Objects.equals(fileUploadDto.getSeqId(), fileUploadSession.getSumPiece())){
            ResultModel<String> resultModel = ResultModel.buildContinueResultModel("文件分片".concat(fileUploadDto.getSeqId().toString()).concat("上传完成！"));
            resultModel.setData(String.valueOf(fileUploadDto.getSeqId()+1));
            return resultModel;
        }

        // 判断文件是否存在
        if(FileUtils.fileExists(realFile)){
            FileUtils.deleteFile(swapFile);
            saveFileData(buildFileDto(fileUploadSession));
            return ResultModel.buildSuccessResultModel("文件上传完成!");
        }

        // 修改上传状态、修改文件名（并发）
        Lock md5Lock = Optional.ofNullable(
                md5LocksMap.putIfAbsent(fileMd5, new ReentrantLock())
        ).orElse(
                md5LocksMap.get(fileMd5)
        );
        try{
            md5Lock.lock();
            if(FileUtils.fileExists(realFile)){
                FileUtils.deleteFile(swapFile);
                saveFileData(buildFileDto(fileUploadSession));
                return ResultModel.buildSuccessResultModel("文件上传完成！");
            }
            fileUploadSessionList = getFileUploadSessionIdListFromCache(fileMd5);
            assert fileUploadSessionList != null;
            fileUploadSessionList.setComplete(true);
            setFileUploadSessionIdListIntoCache(fileUploadSessionList);
            saveFileData(buildFileDto(fileUploadSession));
            swapFile.renameTo(realFile);
            ThreadPoolExecutor threadPoolExecutor = threadPoolFactory.getThreadPoolExecutor();
            threadPoolExecutor.submit(() -> {
                clearFileUploadSession(fileMd5);
            });
            return ResultModel.buildSuccessResultModel("文件上传成功！");
        }finally {
            md5Lock.unlock();
        }
    }

    public FileReferenceEntity buildFileReferenceEntity(FileUploadRequest fileUploadRequest){
        return FileReferenceEntity
                .builder()
                .fileId(snowMaker.nextId())
                .fileMd5(fileUploadRequest.getFileMd5())
                .fileName(fileUploadRequest.getFileName())
                .createdDate(new Date())
                .createdBy(fileUploadRequest.getUserId())
                .build();
    }

    public FileDto buildFileDto(FileUploadSession fileUploadSession){
        return FileDto
                .builder()
                .fileId(snowMaker.nextId())
                .fileName(fileUploadSession.getFilename())
//                .fileSize(fileUploadSession.get)
                .filePath(fileUploadSession.getRealFilePath())
                .fileMd5(fileUploadSession.getFileMd5())
                .createdBy(fileUploadSession.getUserId())
                .createdDate(new Date())
                .build();
    }

    /**
     * 保存文件
     * */
    @Transactional
    public void saveFileData(FileDto fileDto){
        Optional<FileInstanceEntity> fileInstanceEntityOptional = Optional.ofNullable(
                fileInstanceMapper.selectById(fileDto.getFileId())
        );
        if(fileInstanceEntityOptional.isPresent()){
            fileInstanceMapper.updateById(fileInstanceEntityOptional.get());
        } else{
            fileInstanceMapper.insert(fileDto.toFileInstanceEntity());
        }
        fileReferenceMapper.insert(fileDto.toFileReferenceEntity());
    }

    /**
     * 保存文件分片到磁盘中
     * */
    public void saveFilePiece(InputStream sourceInputStream,File targetFile) throws IOException {
        if(!targetFile.exists()){
            if(!targetFile.createNewFile()){
                throw new IOException("create file fail!");
            }
        }
        byte[] buf = new byte[1024];
        BufferedOutputStream targetOutputStream = new BufferedOutputStream(
                new FileOutputStream(targetFile,true)
        );
        while(sourceInputStream.read(buf) != -1){
            targetOutputStream.write(buf);
        }
        targetOutputStream.flush();
        targetOutputStream.close();
    }

    /**
     * 清除文件上传会话
     * */
    public void clearFileUploadSession(String fileMd5){
        Optional<FileUploadSessionList> fileUploadSessionListOptional = Optional.ofNullable(getFileUploadSessionIdListFromCache(fileMd5));
        fileUploadSessionListOptional.ifPresent(fileUploadSessionList -> {
            fileUploadSessionList.getSessionIds().forEach(
                    this::deleteFileUploadSessionFromCache
            );
        });
        deleteFileUploadSessionIdListFromCache(fileMd5);
    }

    /**
     * 将FileUploadSession存入缓存中
     * */
    private void setFileUploadSessionIntoCache(FileUploadSession fileUploadSession){
        redisUtil.set("FILE_UPLOAD_SESSION_".concat(fileUploadSession.getSessionId()),fileUploadSession,10, TimeUnit.MINUTES);
    }

    /**
     * 从缓存中获取FileUploadSession
     * */
    private FileUploadSession getFileUploadSessionFromCache(String sessionId){
        return (FileUploadSession)redisUtil.getForObject("FILE_UPLOAD_SESSION_".concat(sessionId));
    }

    /**
     * 从缓存中删除FileUploadSession
     * */
    private void deleteFileUploadSessionFromCache(String sessionId){
        redisUtil.delete("FILE_UPLOAD_SESSION_".concat(sessionId));
    }

    /**
     * 从缓存中获取SessionID列表
     * */
    private FileUploadSessionList getFileUploadSessionIdListFromCache(String fileMd5){
        Object value = redisUtil.getForObject("FILE_UPLOAD_SESSION_ID_LIST_".concat(fileMd5));
        if(value != null){
            return (FileUploadSessionList)value;
        }
        return null;
    }

    /**
     * 更新SessionID列表
     * */
    private void setFileUploadSessionIdListIntoCache(FileUploadSessionList fileUploadSessionList){
        redisUtil.set(
                "FILE_UPLOAD_SESSION_ID_LIST_".concat(fileUploadSessionList.getFileMd5()),
                fileUploadSessionList
        );
    }

    /**
     * 删除sessionIdList
     * */
    private void deleteFileUploadSessionIdListFromCache(String fileMd5){
        redisUtil.delete("FILE_UPLOAD_SESSION_ID_LIST_".concat(fileMd5));
    }
}
