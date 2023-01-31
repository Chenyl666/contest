package com.contest.async.consumer;

import com.contest.cnst.ChannelNames;
import com.contest.async.consumer.channel.RegisterEmailCodeInputChannel;
import com.contest.dto.notify.EmailCodeMessageDto;
import com.contest.service.RegisterEmailService;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import javax.annotation.Resource;

@EnableBinding(RegisterEmailCodeInputChannel.class)
public class RegisterEmailCodeConsumer {

    @Resource
    RegisterEmailService registerEmailService;

    /**
     * 发送邮箱注册验证码
     * */
    @StreamListener(ChannelNames.CONTEST_REGISTER_CODE)
    public void registerEmailCodeListener(Message<EmailCodeMessageDto> message){
        registerEmailService.sendRegisterEmailCode(message.getPayload());
    }
}
