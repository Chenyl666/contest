package com.contest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.contest.async.processor.channel.AsyncDeleteChannel;
import com.contest.entity.filesys.FileTimeoutEntity;
import com.contest.mapper.FileTimeoutMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileTimeoutService {

    @Resource
    private FileTimeoutMapper fileTimeoutMapper;

    @Resource
    private AsyncDeleteChannel asyncDeleteChannel;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteTimeoutFile(){
        Long timestamp = System.currentTimeMillis();
        List<FileTimeoutEntity> fileTimeoutEntityList = fileTimeoutMapper.selectList(new QueryWrapper<FileTimeoutEntity>().lt("timeout", timestamp));
        List<Long> fileIdList = fileTimeoutEntityList.stream().map(FileTimeoutEntity::getFileId).collect(Collectors.toList());
        fileIdList.forEach(fileId -> {
            asyncDeleteChannel.deleteTimeoutFileOutputChannel().send(
                    MessageBuilder.withPayload(fileId).build()
            );
        });
        log.info("Cleaning timeout file complete!");
    }

    /**
     * 增加临时文件
     * */
    public void addTimeoutFile(FileTimeoutEntity fileTimeoutEntity){
        if(fileTimeoutMapper.insert(fileTimeoutEntity) == 0){
            fileTimeoutMapper.updateById(fileTimeoutEntity);
        }
    }

}
