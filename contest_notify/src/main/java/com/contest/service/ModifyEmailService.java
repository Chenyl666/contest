package com.contest.service;

import com.contest.dto.notify.EmailCodeMessageDto;

public interface ModifyEmailService {
    /**
     * 修改密码邮箱验证码发送服务
     * */
    public void sendModifyEmailCode(EmailCodeMessageDto emailCodeMessageDto);
}
