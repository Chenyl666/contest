package com.contest.service.impl;

import com.contest.config.filesys.FileSystemEnvInfo;
import com.contest.dto.user.UserDto;
import com.contest.entity.filesys.FileInstanceEntity;
import com.contest.entity.filesys.FileReferenceEntity;
import com.contest.mapper.FileInstanceMapper;
import com.contest.mapper.FileReferenceMapper;
import com.contest.service.DownloadService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class DownloadServiceImpl implements DownloadService {

    @Resource
    private FileInstanceMapper fileInstanceMapper;

    @Resource
    private FileReferenceMapper fileReferenceMapper;

    @Resource
    private FileSystemEnvInfo fileSystemEnvInfo;

    @SneakyThrows
    @Override
    public void download(String fileId, HttpServletResponse response, UserDto userDto, boolean isOnline) {
        FileReferenceEntity fileReferenceEntity = fileReferenceMapper.selectById(fileId);
        if(!fileReferenceEntity.isPublic() && (
                userDto == null || !userDto.getUserId().equals(fileReferenceEntity.getCreatedBy()))
        ){
            return;
        }
        FileInstanceEntity fileInstanceEntity = fileInstanceMapper.selectById(fileReferenceEntity.getFileMd5());
        if(!isOnline){
            setDownloadResponseHeader(response, fileReferenceEntity.getFileName(), fileInstanceEntity.getFileSize());
        }
        sendingFileStream(response.getOutputStream(), fileSystemEnvInfo.buildFilePath(fileInstanceEntity.getFileMd5()));
    }

    /**
     * 设置文件下载信息
     * */
    @SneakyThrows
    private void setDownloadResponseHeader(
            HttpServletResponse response,
            String fileName,
            Long fileSize) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setHeader("Content-Length",fileSize.toString());
    }

    /**
     * 发送文件流
     * */
    @SneakyThrows
    public void sendingFileStream(OutputStream respOut, String filePath){
        OutputStream out = new BufferedOutputStream(respOut);
        InputStream in = new BufferedInputStream(Files.newInputStream(Paths.get(filePath)));
        byte[] buf = new byte[1024];
        int bufCnt;
        while((bufCnt = in.read(buf)) != -1){
            out.write(buf,0,bufCnt);
        }
        out.flush();
        in.close();
    }

}
