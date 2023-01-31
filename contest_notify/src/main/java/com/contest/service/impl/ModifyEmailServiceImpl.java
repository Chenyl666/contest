package com.contest.service.impl;

import com.contest.dto.notify.EmailCodeMessageDto;
import com.contest.service.ModifyEmailService;
import com.contest.smtp.SmtpMailSender;
import com.contest.template.EmailCodeTemplate;
import com.contest.template.EmailSubject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ModifyEmailServiceImpl implements ModifyEmailService {

    @Resource
    SmtpMailSender smtpMailSender;

    /**
     * 修改密码邮箱验证码服务
     * */
    @Override
    public void sendModifyEmailCode(EmailCodeMessageDto emailCodeMessageDto) {
        smtpMailSender.sendSimpleEmail(
                EmailSubject.EMAIL_CODE_SUBJECT
                , EmailCodeTemplate.buildEmailCodeMessageContent(
                        EmailCodeTemplate.Operation.OPERATION_MODIFY,emailCodeMessageDto.getCode()
                ), emailCodeMessageDto.getUserEmail());
    }
}
