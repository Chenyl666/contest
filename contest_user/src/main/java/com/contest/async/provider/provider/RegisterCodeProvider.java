package com.contest.async.provider.provider;

import com.contest.async.provider.channel.RegisterCodeOutputChannel;
import com.contest.dto.notify.EmailCodeMessageDto;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import javax.annotation.Resource;

@EnableBinding(RegisterCodeOutputChannel.class)
public class RegisterCodeProvider {
    @Resource
    private RegisterCodeOutputChannel registerCodeOutputChannel;

    /**
     * 异步调用
     * 发送邮箱注册验证码
     * */
    public void sendRegisterEmailCode(EmailCodeMessageDto emailCodeMessageDto){
        registerCodeOutputChannel.registerCodeChannel().send(
                MessageBuilder.withPayload(emailCodeMessageDto).build()
        );
    }

}
