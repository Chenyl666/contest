package com.contest.sso;

import com.contest.dto.user.UserDto;
import lombok.Data;

@Data
public class TokenPayload {
    private UserDto userDto;
    private String ipAddress;
    private Long loginTimer;
    private Long expireTimer;

    public TokenPayload(UserDto userDto, String ipAddress, Long loginTimer) {
        this.userDto = userDto;
        this.ipAddress = ipAddress;
        this.loginTimer = loginTimer;
        this.expireTimer = this.loginTimer + 1000 * 3600 * 24 * 3;
    }

    public Boolean isExpire(){
        return loginTimer > expireTimer;
    }

}
