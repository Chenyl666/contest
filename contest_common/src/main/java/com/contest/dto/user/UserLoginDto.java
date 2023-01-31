package com.contest.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserLoginDto {
    private String userId;
    private String userPwd;
    private String userEmail;
    private String userEmailCode;
    private Integer loginOperation;
}
