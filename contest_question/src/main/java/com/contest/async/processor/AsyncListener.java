package com.contest.async.processor;

import com.contest.async.processor.channel.AsyncChannel;
import com.contest.cnst.ChannelNames;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;

import javax.annotation.Resource;

@EnableBinding(AsyncChannel.class)
public class AsyncListener {

    @Resource
    private AsyncChannel asyncChannel;

    public void setFileNotTimeout(Long fileId){
        asyncChannel.setFileNotTimeout().send(MessageBuilder.<Long>withPayload(fileId).build());
    }
}
