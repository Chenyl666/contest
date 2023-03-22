package com.contest.async.provider;

import com.contest.async.provider.channel.NotifyMessageOutputChannel;
import com.contest.entity.notify.NotifyMessageEntity;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@EnableBinding(NotifyMessageOutputChannel.class)
public class NotifyMessageProvider {

    @Resource
    private NotifyMessageOutputChannel notifyMessageOutputChannel;

    public void asyncSendNotifyMessage(NotifyMessageEntity notifyMessageEntity){
        notifyMessageOutputChannel.notifySendingChannel().send(
                MessageBuilder.withPayload(notifyMessageEntity).build()
        );
    }

}
