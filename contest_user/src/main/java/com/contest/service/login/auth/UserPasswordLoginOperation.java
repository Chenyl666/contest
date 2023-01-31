package com.contest.service.login.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.contest.dto.user.UserLoginDto;
import com.contest.entity.user.UserEntity;
import com.contest.mapper.UserMapper;
import com.contest.result.ResultFlag;
import com.contest.result.ResultModel;
import com.contest.sso.JwtUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 以账号和密码的方式登录
 * */
@Component
public class UserPasswordLoginOperation implements LoginOperation{

    @Resource
    private UserMapper userMapper;

    /**
     * 1. 获取登录信息
     * 2. 验证用户信息
     * 3. 生成token
     * 4. 返回token
     * */
    @Override
    public ResultModel<String> login(UserLoginDto userLoginDto,String ip) {
        Optional<UserEntity> userEntityOptional = Optional.ofNullable(userMapper.selectOne(
                new QueryWrapper<UserEntity>()
                        .eq("user_id", userLoginDto.getUserId())
                        .eq("user_pwd", userLoginDto.getUserPwd())
                        .or(qw -> {
                            qw.eq("user_id", userLoginDto.getUserId()).eq("user_pwd", userLoginDto.getUserPwd());
                        })
        ));
        if(!userEntityOptional.isPresent()){
            return ResultModel.buildFailResultModel("密码输入错误！");
        }
        return ResultModel.<String>builder()
                .data(JwtUtil.generateToken(userEntityOptional.get().entity2Dto(), ip))
                .message("登录成功！")
                .resultFlag(ResultFlag.SUCCESS)
                .resultCode(ResultFlag.SUCCESS.code)
                .build();
    }
}
