package com.contest.async.consumer.channel;

import com.contest.cnst.ChannelNames;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface RegisterEmailCodeInputChannel {
    @Input(ChannelNames.CONTEST_REGISTER_CODE)
    SubscribableChannel registerEmailCodeChannel();
}
