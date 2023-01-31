package com.contest.dto.notify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EmailCodeMessageDto {
    // 用户接收邮箱
    private String userEmail;
    // 邮箱验证码
    private String code;
}
