package com.contest.async.consumer.channel;

import com.contest.cnst.ChannelNames;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface LoginEmailCodeInputChannel {
    @Input(ChannelNames.CONTEST_LOGIN_CODE)
    public SubscribableChannel loginEmailCodeInputChannel();
}
