package com.contest.service.login.auth;

import com.contest.dto.user.UserLoginDto;
import com.contest.result.ResultModel;

public interface LoginOperation {

    public static final Integer LOGIN_BY_USER_PASSWORD = 0;

    public static final Integer LOGIN_BY_EMAIL_PASSWORD = 1;

    public static final Integer LOGIN_BY_EMAIL_CODE = 2;

    public ResultModel<String> login(UserLoginDto userLoginDto,String ip);



}
