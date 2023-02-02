package com.contest.service.upload.impl;

import com.contest.entity.filesys.FileInstanceEntity;
import com.contest.mapper.FileInstanceMapper;
import com.contest.result.ResultModel;
import com.contest.service.upload.UploadService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {

    @Resource
    private FileInstanceMapper fileInstanceMapper;

    /**
     * 请求上传文件
     * 1 通过md5标识码检查文件是否存在
     * 2.1 如果存在则文件上传完成
     * 2.2 如果不存在则返回UUID作为文件上传标识
     *
     * */
    @Override
    public ResultModel<String> requestConcurrentUpload(String fileMd5) {
        Optional<FileInstanceEntity> fileInstanceEntityOptional = Optional.ofNullable(fileInstanceMapper.selectById(fileMd5));
        if(fileInstanceEntityOptional.isPresent()){
            return ResultModel.buildSuccessResultModel("文件上传成功！");
        }
        ResultModel<String> resultModel = ResultModel.buildContinueResultModel();
        resultModel.setData(UUID.randomUUID().toString());
        return resultModel;
    }
}
