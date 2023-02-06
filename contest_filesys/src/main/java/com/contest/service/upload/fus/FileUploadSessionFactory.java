package com.contest.service.upload.fus;

import com.contest.config.filesys.FileSystemEnvInfo;
import com.contest.dto.user.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.contest.dto.filesys.FileUploadRequest;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * FileUploadSession单例工厂（懒汉模式）
 * */
@Component
public class FileUploadSessionFactory {

    @Resource
    private FileSystemEnvInfo fileSystemEnvInfo;

    /**
     * 创建FileUploadSession对象
     * */
    public FileUploadSession createFileUploadSession(FileUploadRequest fileUploadRequest){
        String sessionId = UUID.randomUUID().toString();
        return new FileUploadSession(
                sessionId,
                fileUploadRequest.getFileMd5(),
                fileSystemEnvInfo.buildFilePath(sessionId),
                fileSystemEnvInfo.buildFilePath(fileUploadRequest.getFileMd5()),
                fileUploadRequest.getSumPiece(),
                fileUploadRequest.getPieceSize(),
                fileUploadRequest.getFileName(),
                fileUploadRequest.getUserId()
        );
    }

}
