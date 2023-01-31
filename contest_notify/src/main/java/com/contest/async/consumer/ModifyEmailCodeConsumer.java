package com.contest.async.consumer;

import com.contest.async.consumer.channel.ModifyEmailCodeInputChannel;
import com.contest.cnst.ChannelNames;
import com.contest.dto.notify.EmailCodeMessageDto;
import com.contest.service.ModifyEmailService;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import javax.annotation.Resource;

@EnableBinding(ModifyEmailCodeInputChannel.class)
public class ModifyEmailCodeConsumer {

    @Resource
    private ModifyEmailService modifyEmailService;

    /**
     * 发送修改密码时的邮箱验证码
     * */
    @StreamListener(ChannelNames.CONTEST_MODIFY_CODE)
    public void sendModifyPasswordEmailCode(Message<EmailCodeMessageDto> message){
        modifyEmailService.sendModifyEmailCode(message.getPayload());
    }
}
