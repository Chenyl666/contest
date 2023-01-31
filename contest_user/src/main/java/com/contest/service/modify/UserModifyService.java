package com.contest.service.modify;

import com.contest.dto.ModifyPwdDto;
import com.contest.result.ResultModel;

public interface UserModifyService {

    /**
     * 发送修改密码时的邮箱验证码
     * */
    public ResultModel<String> sendModifyPasswordEmailCode(String userEmail);

    /**
     * 保存修改密码信息
     * */
    public ResultModel<String> saveModifyPassword(ModifyPwdDto modifyPwdDto);
}
