package com.contest.async.provider.sender;

import com.contest.async.provider.channel.ModifyCodeOutputChannel;
import com.contest.dto.notify.EmailCodeMessageDto;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;

import javax.annotation.Resource;

@EnableBinding(ModifyCodeOutputChannel.class)
public class ModifyCodeProvider {
    @Resource
    ModifyCodeOutputChannel modifyCodeOutputChannel;

    public void sendModifyPasswordEmailCode(EmailCodeMessageDto emailCodeMessageDto){
        modifyCodeOutputChannel.modifyCodeChannel().send(
                MessageBuilder.withPayload(emailCodeMessageDto).build()
        );
    }
}
