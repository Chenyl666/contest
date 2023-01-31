package com.contest.service;

import com.contest.dto.notify.EmailCodeMessageDto;

public interface LoginEmailService {
    /**
     * 登录邮箱验证码发送服务
     * */
    public void sendLoginEmailCode(EmailCodeMessageDto emailCodeMessageDto);
}
