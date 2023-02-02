package com.contest.service.upload.fus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * FileUploadSession单例工厂（懒汉模式）
 * */
@Component
public class FileUploadSessionFactory {
    private static volatile FileUploadSessionFactory fileUploadSessionFactory;

    @Value("${filesys.path}")
    private String filePath;

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
    public FileUploadSession createFileUploadSessionIfFileNotExist(File targetFile, InputStream sourceInputStream){
        return null;
    }

}
