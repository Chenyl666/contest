package com.contest.service.upload;

import com.contest.result.ResultModel;

import java.io.InputStream;

public interface UploadService {
    /**
     * 请求上传文件
     * */
    public ResultModel<String> requestConcurrentUpload(String fileMd5);

    /**
     * 文件分片断点续传
     * */
    public ResultModel<String> uploadFilePiece();

}
