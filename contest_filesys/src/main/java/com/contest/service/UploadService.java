package com.contest.service;

import com.contest.dto.filesys.FileTextDto;
import com.contest.dto.filesys.FileUploadDto;
import com.contest.dto.filesys.SimpleFileUploadDto;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import com.contest.dto.filesys.FileUploadRequest;

import java.io.InputStream;

public interface UploadService {
    /**
     * 请求上传文件
     * */
    public ResultModel<String> requestConcurrentUpload(FileUploadRequest fileUploadRequest);

    /**
     * 文件分片断点续传
     * */
    public ResultModel<String> uploadFilePiece(FileUploadDto fileUploadDto);

    /**
     * 上传小文件
     * */
    public ResultModel<String> uploadSimpleFile(UserDto userDto,SimpleFileUploadDto simpleFileUploadDto);

    /**
     * 上传文本文件
     * */
    public ResultModel<String> uploadText(FileTextDto fileTextDto,UserDto userDto);
}
