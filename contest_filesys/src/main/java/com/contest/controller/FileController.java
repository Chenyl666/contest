package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.annotation.filepiece.FilePiece;
import com.contest.annotation.uploadrequest.UploadRequest;
import com.contest.dto.filesys.FileUploadDto;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import com.contest.service.upload.UploadService;
import com.contest.dto.filesys.FileUploadRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/filesys")
public class FileController {

    @Resource
    private UploadService uploadService;

    /**
     * 请求文件上传
     * */
    @PostMapping("/upload/request")
    public ResultModel<String> requestUploadFile(
            @UploadRequest FileUploadRequest fileUploadRequest
    ){
        return uploadService.requestConcurrentUpload(fileUploadRequest);
    }


    @PostMapping("/upload/file")
    public ResultModel<String> uploadFilePiece(@FilePiece FileUploadDto fileUploadDto){
        return uploadService.uploadFilePiece(fileUploadDto);
    }
}
