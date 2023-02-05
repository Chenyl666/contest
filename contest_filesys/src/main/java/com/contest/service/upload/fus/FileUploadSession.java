package com.contest.service.upload.fus;

import com.contest.dto.user.UserDto;
import com.contest.util.FileUtils;
import com.contest.util.Md5Utils;
import lombok.*;

import javax.servlet.ServletInputStream;
import java.io.*;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * 文件上传会话session
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadSession implements Serializable{

    private static final Map<String,Lock> md5LocksMap = new ConcurrentHashMap<>();

    public static class FileUploadException extends RuntimeException{
        public FileUploadException(String message) {
            super(message);
        }
    }

    /**
     * 会话id
     * */
    private String sessionId;

    /**
     * 文件的MD5标识码
     * */
    private String fileMd5;

    /**
     * 临时文件路径
     * */
    private String swapFilePath;

    /**
     * 最终文件路径
     * */
    private String realFilePath;

    /**
     * 是否完成传输标志位
     * */
    private Boolean isComplete;

    /**
     * 总分片数
     * */
    private Integer sumPiece;

    /**
     * 分片大小
     * */
    private Long pieceSize;

    /**
     * 文件名
     * */
    private String filename;

    /**
     * 文件所属用户
     * */
    private String userId;

    public FileUploadSession(
            String sessionId,
            String fileMd5,
            String swapFilePath,
            String realFilePath,
            Integer sumPiece,
            Long pieceSize,
            String filename,
            String userId
    ) {
        this.sessionId = sessionId;
        this.fileMd5 = fileMd5;
        this.swapFilePath = swapFilePath;
        this.realFilePath = realFilePath;
        this.isComplete = false;
        this.sumPiece = sumPiece;
        this.pieceSize = pieceSize;
        this.filename = filename;
        this.userId = userId;
    }

}
