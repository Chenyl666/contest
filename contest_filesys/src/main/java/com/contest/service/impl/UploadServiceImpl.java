package com.contest.service.impl;

import com.contest.result.ResultModel;
import com.contest.service.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class UploadServiceImpl implements UploadService {

    @Value("${filesys.path}")
    private String filePath;

    /**
     * 上传简单文件
     * 1. 保存文件到系统（UUID命名）
     * 2. 生成文件的MD5标识码
     * 3. 更新文件名为MD5标识码
     * 4. 将文件信息保存到数据库
     * */
    @Override
    public ResultModel<String> uploadSimpleFile(InputStream in) {

        return null;
    }
}
