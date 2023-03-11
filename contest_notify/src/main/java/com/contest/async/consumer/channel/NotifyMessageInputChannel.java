package com.contest.async.consumer.channel;

import com.contest.cnst.ChannelNames;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface NotifyMessageInputChannel {

    @Input(ChannelNames.CONTEST_NOTIFY_SENDING)
    public SubscribableChannel notifySendingChannel();

}
