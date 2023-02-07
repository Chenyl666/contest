package com.contest.async.processor;

import com.contest.async.processor.channel.AsyncDeleteChannel;
import com.contest.cnst.ChannelNames;
import com.contest.entity.filesys.FileReferenceEntity;
import com.contest.service.DeleteService;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import javax.annotation.Resource;

@EnableBinding(AsyncDeleteChannel.class)
public class AsyncDeleteListener {
    @Resource
    private DeleteService deleteService;

    @StreamListener(ChannelNames.CONTEST_ASYNC_DELETE_FILE)
    public void asyncDeleteFile(Message<FileReferenceEntity> message){
        deleteService.deleteFile(message.getPayload());
    }
}
