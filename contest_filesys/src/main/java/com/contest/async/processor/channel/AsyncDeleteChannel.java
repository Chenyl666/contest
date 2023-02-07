package com.contest.async.processor.channel;

import com.contest.cnst.ChannelNames;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

public interface AsyncDeleteChannel {
    @Output(ChannelNames.CONTEST_ASYNC_DELETE_FILE)
    public SubscribableChannel asyncDeleteFileOutputChannel();

    @Input(ChannelNames.CONTEST_ASYNC_DELETE_FILE)
    public SubscribableChannel asyncDeleteFileInputChannel();
}
