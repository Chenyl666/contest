package com.contest.async.processor;

import com.contest.async.processor.channel.AsyncChannel;
import com.contest.cnst.ChannelNames;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(AsyncChannel.class)
public class AsyncListener {

}
