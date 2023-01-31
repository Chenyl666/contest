package com.contest.async.consumer;

import com.contest.async.consumer.channel.LoginEmailCodeInputChannel;
import com.contest.cnst.ChannelNames;
import com.contest.dto.notify.EmailCodeMessageDto;
import com.contest.service.LoginEmailService;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import javax.annotation.Resource;

@EnableBinding(LoginEmailCodeInputChannel.class)
public class LoginEmailCodeConsumer {

    @Resource
    LoginEmailService loginEmailService;

    /**
     * 发送邮箱登录验证码
     * */
    @StreamListener(ChannelNames.CONTEST_LOGIN_CODE)
    public void loginEmailCodeListener(Message<EmailCodeMessageDto> message){
        loginEmailService.sendLoginEmailCode(message.getPayload());
    }

}
