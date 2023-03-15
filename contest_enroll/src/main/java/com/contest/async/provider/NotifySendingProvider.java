package com.contest.async.provider;

import com.contest.async.channel.NotifySendingOutputChannel;
import com.contest.entity.notify.NotifyMessageEntity;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;

import javax.annotation.Resource;

@EnableBinding(NotifySendingOutputChannel.class)
public class NotifySendingProvider {
    @Resource
    private NotifySendingOutputChannel notifySendingOutputChannel;

    public void sendNotifyMessage(NotifyMessageEntity notifyEntity){
        notifySendingOutputChannel.notifySendingChannel().send(
                MessageBuilder.<NotifyMessageEntity>withPayload(notifyEntity).build()
        );
    }
}
