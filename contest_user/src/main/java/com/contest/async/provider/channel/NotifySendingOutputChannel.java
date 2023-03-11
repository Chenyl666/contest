package com.contest.async.provider.channel;

import com.contest.cnst.ChannelNames;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

public interface NotifySendingOutputChannel {

    @Output(ChannelNames.CONTEST_NOTIFY_SENDING)
    public SubscribableChannel notifySendingChannel();

}
