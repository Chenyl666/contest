package com.contest.service.login;

import com.contest.dto.user.UserLoginDto;
import com.contest.result.ResultModel;

public interface LoginService {
    public ResultModel<String> sendUserLoginEmailCode(String userEmail);

    public ResultModel<String> login(UserLoginDto userLoginDto,String ip);
}
