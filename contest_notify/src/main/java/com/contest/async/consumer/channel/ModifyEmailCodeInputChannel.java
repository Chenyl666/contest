package com.contest.async.consumer.channel;

import com.contest.cnst.ChannelNames;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ModifyEmailCodeInputChannel {
    @Input(ChannelNames.CONTEST_MODIFY_CODE)
    public SubscribableChannel modifyEmailCodeChannel();
}
