package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.annotation.filepiece.FilePiece;
import com.contest.annotation.simplefile.SimpleFileRequestBody;
import com.contest.annotation.uploadrequest.UploadRequest;
import com.contest.dto.filesys.FileUploadDto;
import com.contest.dto.filesys.SimpleFileUploadDto;
import com.contest.dto.user.UserDto;
import com.contest.result.FullAdapterResult;
import com.contest.result.ResultModel;
import com.contest.service.DeleteService;
import com.contest.service.DownloadService;
import com.contest.service.UploadService;
import com.contest.dto.filesys.FileUploadRequest;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

@RestController
@RequestMapping("/filesys")
public class FileController {

    @Resource
    private UploadService uploadService;

    @Resource
    private DownloadService downloadService;

    @Resource
    private DeleteService deleteService;

    /**
     * 请求文件上传
     * */
    @PostMapping("/upload/request")
    public ResultModel<String> requestUploadFile(@UploadRequest FileUploadRequest fileUploadRequest){
        return uploadService.requestConcurrentUpload(fileUploadRequest);
    }

    /**
     * 上传文件分片
     * */
    @PostMapping("/upload/file")
    public ResultModel<String> uploadFilePiece(@FilePiece FileUploadDto fileUploadDto){
        return uploadService.uploadFilePiece(fileUploadDto);
    }

    /**
     * 上传简单文件
     * */
    @PostMapping("/upload/simple")
    public ResultModel<String> uploadSimpleFile(
            @CurrentUser UserDto userDto,
            @SimpleFileRequestBody SimpleFileUploadDto simpleFileUploadDto
    ){
        return uploadService.uploadSimpleFile(userDto, simpleFileUploadDto);
    }

    /**
     * 上传富文本编辑器文件
     * */
    @SneakyThrows
    @PostMapping("/upload/fulltext")
    public FullAdapterResult uploadFullTextFile(
            @CurrentUser UserDto userDto,
            @SimpleFileRequestBody SimpleFileUploadDto simpleFileUploadDto
    ){
        return new FullAdapterResult(uploadSimpleFile(userDto, simpleFileUploadDto));
    }

    /**
     * 下载附件
     * */
    @GetMapping("/download/attachment/{file_id}")
    public void downloadAttachmentFile(
            @PathVariable("file_id") String fileId,
            @CurrentUser UserDto userDto,
            HttpServletResponse response
    ){
        downloadService.download(fileId, response, userDto, false);
    }

    /**
     * 加载文件
     * */
    @GetMapping("/download/inline/{file_id}")
    public void downloadInlineFile(
            @PathVariable("file_id") String fileId,
            @CurrentUser UserDto userDto,
            HttpServletResponse response
    ){
        downloadService.download(fileId, response, userDto, true);
    }

    /**
     * 删除文件
     * */
    @DeleteMapping("/delete/{file_id}")
    public ResultModel<String> deleteFile(@PathVariable("file_id") String fileId, @CurrentUser UserDto userDto){
        return deleteService.requestDeleteFile(fileId, userDto);
    }

}
