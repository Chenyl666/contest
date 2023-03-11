package com.contest.async.consumer;

import com.contest.async.consumer.channel.NotifyMessageInputChannel;
import com.contest.cnst.ChannelNames;
import com.contest.entity.notify.NotifyMessageEntity;
import com.contest.service.NotifyService;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import javax.annotation.Resource;

@EnableBinding(NotifyMessageInputChannel.class)
public class NotifyMessageConsumer {

    @Resource
    private NotifyService notifyService;

    @StreamListener(ChannelNames.CONTEST_NOTIFY_SENDING)
    public void sendNotifyMessage(Message<NotifyMessageEntity> message){
        notifyService.saveNotifyMessage(message.getPayload());
    }

}
