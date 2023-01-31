package com.contest.service.impl;

import com.contest.dto.notify.EmailCodeMessageDto;
import com.contest.service.RegisterEmailService;
import com.contest.smtp.SmtpMailSender;
import com.contest.template.EmailCodeTemplate;
import com.contest.template.EmailSubject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RegisterEmailServiceImpl implements RegisterEmailService {

    @Resource
    SmtpMailSender smtpMailSender;

    /**
     * 注册邮箱验证码服务
     * */
    @Override
    public void sendRegisterEmailCode(EmailCodeMessageDto emailCodeMessageDto) {
        smtpMailSender.sendSimpleEmail(
                EmailSubject.EMAIL_CODE_SUBJECT
                , EmailCodeTemplate.buildEmailCodeMessageContent(
                        EmailCodeTemplate.Operation.OPERATION_REGISTER,emailCodeMessageDto.getCode()
                ), emailCodeMessageDto.getUserEmail());
    }
}
