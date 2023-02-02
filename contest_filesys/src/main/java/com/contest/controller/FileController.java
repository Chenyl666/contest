package com.contest.controller;

import com.contest.result.ResultModel;
import com.contest.service.upload.UploadService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.IOException;

@RestController
public class FileController {

    @Resource
    private UploadService uploadService;

    /**
     * 上传简单文件接口
     * (仅允许上传小文件)
     * */
    public ResultModel<String> uploadSimpleFile(HttpServletRequest request) throws IOException {
        return uploadService.uploadSimpleFile(new BufferedInputStream(request.getInputStream()));
    }
}
