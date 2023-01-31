package com.contest.pojo.file;

import lombok.Data;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 文件上传会话session
 * */
@Data
public class FileUploadSession {
    /**
     * 保存文件的md5-session映射
     * */
    private final Map<String,FileUploadSession> fileUploadSessionExclusiveMap;

    /**
     * 文件上传session等待列表
     * */
    private final Map<String,BlockingQueue<FileUploadSession>> fileUploadSessionWaitingList;

    /**
     * 将要上传的文件
     * */
    private final File file;

    /**
     * 文件流
     * */
    private final InputStream in;

    /**
     * 文件的MD5标识码
     * */
    private String fileMd5;

    /**
     * 上传状态
     * */
    private SessionStatus status;

    private Thread currentThread;

    /**
     * session状态
     * WAITING： 等待状态
     * UPLOADING： 上传状态
     * CANCEL: 撤销状态
     * */
    private enum SessionStatus{
        WAITING,
        UPLOADING,
        CANCEL
    }

    protected FileUploadSession(
            File file,
            InputStream in,
            Map<String,FileUploadSession> fileUploadSessionExclusiveMap,
            Map<String, BlockingQueue<FileUploadSession>> fileUploadSessionWaitingList
    ) {
        this.file = file;
        this.in = in;
        this.fileUploadSessionExclusiveMap = fileUploadSessionExclusiveMap;
        this.fileUploadSessionWaitingList = fileUploadSessionWaitingList;
        this.currentThread = Thread.currentThread();
    }

    /**
     * 判断是否已经存在相同文件的上传会话
     * */
    public boolean hasSessionExist(){
        return fileUploadSessionExclusiveMap.containsKey(fileMd5);
    }

    public void creatFileIfNotExist(File file){
    }

    /**
     * 保存文件
     * */
    public boolean concurrentSaveFile(){
        if(hasSessionExist()){
            this.status = SessionStatus.WAITING;
            Optional<BlockingQueue<FileUploadSession>> blockingQueueOptional = Optional.ofNullable(
                    fileUploadSessionWaitingList.putIfAbsent(fileMd5, new LinkedBlockingQueue<>())
            );
            BlockingQueue<FileUploadSession> blockingQueue = blockingQueueOptional.orElse(fileUploadSessionWaitingList.get(fileMd5));
            try{
                blockingQueue.add(this);
            }catch (Exception e){
                return false;
            }
            LockSupport.park();
            if(this.status == SessionStatus.UPLOADING){
                return writeFileIntoDisk();
            }
            return true;
        } else{
            Optional<FileUploadSession> fileUploadSessionOptional = Optional.ofNullable(
                    fileUploadSessionExclusiveMap.putIfAbsent(fileMd5, this)
            );

            FileUploadSession fileUploadSession = fileUploadSessionOptional.orElse(fileUploadSessionExclusiveMap.get(fileMd5));
            return writeFileIntoDisk();
        }
    }

    @SneakyThrows
    private boolean writeFileIntoDisk(){
        synchronized (fileUploadSessionExclusiveMap.get(fileMd5)){
            if(fileUploadSessionExclusiveMap.containsKey(fileMd5)){
                OutputStream out = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
                byte[] buf = new byte[1024];
                creatFileIfNotExist(file);
                while(in.read(buf)!=-1){
                    out.write(buf);
                }
                out.flush();
                out.close();
            }
        }
        return true;
    }
}
