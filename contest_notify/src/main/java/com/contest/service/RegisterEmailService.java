package com.contest.service;

import com.contest.dto.notify.EmailCodeMessageDto;

public interface RegisterEmailService {
    /**
     * 注册邮箱验证码发送服务
     * */
    public void sendRegisterEmailCode(EmailCodeMessageDto emailCodeMessageDto);
}
