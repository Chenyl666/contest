package com.contest.async.provider.channel;

import com.contest.cnst.ChannelNames;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

public interface ModifyCodeOutputChannel {
    @Output(ChannelNames.CONTEST_MODIFY_CODE)
    SubscribableChannel modifyCodeChannel();
}
