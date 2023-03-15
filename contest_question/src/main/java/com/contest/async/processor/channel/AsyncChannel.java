package com.contest.async.processor.channel;

import com.contest.cnst.ChannelNames;
import com.rabbitmq.client.impl.ChannelN;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.SubscribableChannel;

public interface AsyncChannel {
    @Output(ChannelNames.CONTEST_DELETE_FILE_BY_DOWNLOAD_URL)
    public SubscribableChannel deleteFileByDownloadUrl();

    @Output(ChannelNames.CONTEST_SET_FILE_NOT_TIMEOUT)
    public SubscribableChannel setFileNotTimeout();
}
