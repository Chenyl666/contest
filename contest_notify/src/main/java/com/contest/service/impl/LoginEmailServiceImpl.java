package com.contest.service.impl;

import com.contest.dto.notify.EmailCodeMessageDto;
import com.contest.service.LoginEmailService;
import com.contest.smtp.SmtpMailSender;
import com.contest.template.EmailCodeTemplate;
import com.contest.template.EmailSubject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginEmailServiceImpl implements LoginEmailService {

    @Resource
    SmtpMailSender smtpMailSender;

    /**
     * 登录邮箱验证码服务
     * */
    @Override
    public void sendLoginEmailCode(EmailCodeMessageDto emailCodeMessageDto) {
        smtpMailSender.sendSimpleEmail(
                EmailSubject.EMAIL_CODE_SUBJECT
                , EmailCodeTemplate.buildEmailCodeMessageContent(
                        EmailCodeTemplate.Operation.OPERATION_LOGIN,emailCodeMessageDto.getCode())
                , emailCodeMessageDto.getUserEmail());
    }
}
