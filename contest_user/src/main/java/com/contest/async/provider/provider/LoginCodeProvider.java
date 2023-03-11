package com.contest.async.provider.provider;

import com.contest.async.provider.channel.LoginCodeOutputChannel;
import com.contest.dto.notify.EmailCodeMessageDto;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;

import javax.annotation.Resource;

@EnableBinding(LoginCodeOutputChannel.class)
public class LoginCodeProvider {
    @Resource
    LoginCodeOutputChannel loginCodeOutputChannel;

    /**
     * 异步调用
     * 发送邮箱登录验证码
     * */
    public void sendLoginEmailCode(EmailCodeMessageDto emailCodeMessageDto){
        loginCodeOutputChannel.loginCodeChannel().send(
                MessageBuilder.withPayload(emailCodeMessageDto).build()
        );
    }

}
