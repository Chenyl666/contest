package com.contest.service.upload.fus;

import com.contest.util.Md5Utils;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import javax.servlet.ServletInputStream;
import java.io.*;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * 文件上传会话session
 * */
@Data
@Builder
public class FileUploadSession implements Serializable{

    private static final Map<String,Lock> md5LocksMap = new ConcurrentHashMap<>();

    /**
     * 将要上传的文件
     * */
    private final File targetFile;

    /**
     * 文件流
     * */
    private final InputStream sourceInputStream;

    /**
     * 文件的MD5标识码
     * */
    private String fileMd5;

    /**
     * 文件保存路径
     * */
    private String savePath;

    /**
     * 并发传输是否完成
     * */
    private Boolean isComplete;

    protected FileUploadSession(File targetFile,InputStream sourceInputStream) {
        this.targetFile = targetFile;
        this.sourceInputStream = sourceInputStream;
        this.fileMd5 = Md5Utils.getFileMd5(sourceInputStream);
        this.isComplete = false;
    }

    /**
     * 保存文件
     * */
    public int concurrentSaveFile(){
        return 0;
    }

}
