package com.contest.service.register;

import com.contest.dto.user.UserRegisterDto;
import com.contest.result.ResultModel;

public interface RegisterService {
    /**
     * 发送邮箱注册验证码
     * */
    public ResultModel<String> sendRegisterEmailCode(String userEmail);

    /**
     * 保存用户注册信息
     * */
    public ResultModel<String> registerUser(UserRegisterDto userRegisterDto);

    /**
     * 检查邮箱是否已经被注册
     * */
    public ResultModel<String> checkingEmailExistOrNot(String userEmail);

    /**
     * 检查用户id是否已经被注册
     * */
    public ResultModel<String> checkingUserIdExistOrNot(String userId);
}
