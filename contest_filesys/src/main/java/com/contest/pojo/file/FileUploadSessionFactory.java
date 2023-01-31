package com.contest.pojo.file;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FileUploadSession单例工厂（懒汉模式）
 * */
public class FileUploadSessionFactory {

    private static volatile FileUploadSessionFactory fileUploadSessionFactory;

    private final Map<String,FileUploadSession> fileUploadSessionExclusiveMap = new ConcurrentHashMap<>();

    private final Map<String, BlockingQueue<FileUploadSession>> fileUploadSessionWaitingList = new ConcurrentHashMap<>();

    private FileUploadSessionFactory(){}

    /**
     * 懒汉模式双检查保证线程安全
     * */
    public static FileUploadSessionFactory getFileUploadSessionFactory(){
        if(fileUploadSessionFactory == null){
            synchronized (FileUploadSessionFactory.class){
                if(fileUploadSessionFactory == null){
                    fileUploadSessionFactory = new FileUploadSessionFactory();

                }
            }
        }
        return fileUploadSessionFactory;
    }

    /**
     * 创建FileUploadSession对象
     * */
    public FileUploadSession buildFileUploadSession(File file, InputStream in){
        return new FileUploadSession(file,in,fileUploadSessionExclusiveMap,fileUploadSessionWaitingList);
    }

}
