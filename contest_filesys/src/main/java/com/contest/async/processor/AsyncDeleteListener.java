package com.contest.async.processor;

import com.alibaba.fastjson2.JSON;
import com.contest.async.processor.channel.AsyncDeleteChannel;
import com.contest.cnst.ChannelNames;
import com.contest.dto.user.UserDto;
import com.contest.entity.filesys.FileReferenceEntity;
import com.contest.service.DeleteService;
import com.contest.service.impl.FileTimeoutService;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import javax.annotation.Resource;
import java.util.Map;

@EnableBinding(AsyncDeleteChannel.class)
public class AsyncDeleteListener {
    @Resource
    private DeleteService deleteService;

    @Resource
    private FileTimeoutService fileTimeoutService;

    @StreamListener(ChannelNames.CONTEST_ASYNC_DELETE_FILE)
    public void asyncDeleteFile(Message<FileReferenceEntity> message){
        deleteService.deleteFile(message.getPayload());
    }

    @StreamListener(ChannelNames.CONTEST_DELETE_TIMEOUT_FILE)
    public void deleteTimeoutFile(Message<String> message){
        deleteService.deleteBatchFileOfTimeout(message.getPayload());
    }

    @StreamListener(ChannelNames.CONTEST_DELETE_FILE_BY_DOWNLOAD_URL)
    public void deleteFileByDownloadUrl(Message<Map<String,String>> message){
        Map<String, String> payload = message.getPayload();
        UserDto userDto = JSON.parseObject(payload.get("userDto"),UserDto.class);
        String url = payload.get("url");
        deleteService.deleteFileByDownloadUrl(url,userDto);
    }

    @StreamListener(ChannelNames.CONTEST_SET_FILE_NOT_TIMEOUT)
    public void setFileNotTimeout(Message<Long> message){
        fileTimeoutService.setTimeoutFileCancelTimeOut(message.getPayload());
    }
}
