package com.contest.service.impl;

import com.contest.async.processor.channel.AsyncDeleteChannel;
import com.contest.dto.user.UserDto;
import com.contest.entity.filesys.FileInstanceEntity;
import com.contest.entity.filesys.FileReferenceEntity;
import com.contest.mapper.FileInstanceMapper;
import com.contest.mapper.FileReferenceMapper;
import com.contest.mapper.FileTimeoutMapper;
import com.contest.result.ResultModel;
import com.contest.service.DeleteService;
import com.contest.service.md5lock.Md5Lock;
import com.contest.util.FileUtils;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.Optional;

@Service
public class DeleteServiceImpl implements DeleteService {

    @Resource
    private FileInstanceMapper fileInstanceMapper;

    @Resource
    private FileReferenceMapper fileReferenceMapper;

    @Resource
    private AsyncDeleteChannel asyncDeleteChannel;

    @Resource
    private FileTimeoutMapper fileTimeoutMapper;

    @Resource
    private Md5Lock md5Lock;

    /**
     * 请求删除文件
     * */
    @Override
    @Transactional
    public ResultModel<String> requestDeleteFile(String fileId, UserDto userDto) {
        if(userDto == null){
            return ResultModel.buildFailResultModel("文件删除失败！");
        }
        FileReferenceEntity fileReferenceEntity = fileReferenceMapper.selectById(fileId);
        if(fileReferenceEntity == null || !fileReferenceEntity.getCreatedBy().equals(userDto.getUserId())){
            return ResultModel.buildFailResultModel("没有权限！");
        }
        fileReferenceMapper.deleteById(fileId);
        asyncDeleteChannel.asyncDeleteFileOutputChannel().send(MessageBuilder.<FileReferenceEntity>withPayload(fileReferenceEntity).build());
        return ResultModel.buildSuccessResultModel();
    }

    /**
     * 删除文件实例
     * */
    @Override
    @Transactional
    public void deleteFile(FileReferenceEntity fileReferenceEntity) {
        String fileMd5 = fileReferenceEntity.getFileMd5();
        fileInstanceMapper.decrReferenceNum(fileReferenceEntity.getFileMd5());
        FileInstanceEntity fileInstanceEntity = fileInstanceMapper.selectById(fileReferenceEntity.getFileMd5());
        if(fileInstanceEntity.getReferenceNum() <= 0){
            lockMd5(fileMd5);
            try{
                File targetFile = new File(fileInstanceEntity.getFilePath());
                fileInstanceEntity = fileInstanceMapper.selectById(fileReferenceEntity.getFileMd5());
                if(fileInstanceEntity.getReferenceNum() == 0 && targetFile.exists()){
                    fileInstanceMapper.deleteById(fileMd5);
                    FileUtils.deleteFile(targetFile);
                }
            }finally {
                unlockMd5(fileMd5);
            }
        }
    }

    /**
     * 定时任务删除文件
     * */
    @Override
    public void deleteBatchFileOfTimeout(String fileId) {
        Optional<FileReferenceEntity> fileReferenceEntityOptional = Optional.ofNullable(fileReferenceMapper.selectById(fileId));
        fileReferenceEntityOptional.ifPresent(fileReferenceEntity -> {
            fileReferenceMapper.deleteById(fileId);
            deleteFile(fileReferenceEntity);
        });
        fileTimeoutMapper.deleteById(fileId);
    }

    /**
     * 通过下载链接删除文件
     * */
    @Override
    public void deleteFileByDownloadUrl(String url,UserDto userDto) {
        String[] split = url.split("/");
        String fileId = split[split.length-1];
        requestDeleteFile(fileId,userDto);
    }

    public void lockMd5(String fileMd5){
        md5Lock.lock(fileMd5);
    }

    public void unlockMd5(String fileMd5){
        md5Lock.unlock(fileMd5);
    }
}
