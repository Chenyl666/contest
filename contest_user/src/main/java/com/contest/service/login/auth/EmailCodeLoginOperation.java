package com.contest.service.login.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.contest.result.util.RedisUtil;
import com.contest.dto.user.UserLoginDto;
import com.contest.entity.user.UserEntity;
import com.contest.exception.UserNotFoundException;
import com.contest.mapper.UserMapper;
import com.contest.result.ResultFlag;
import com.contest.result.ResultModel;
import com.contest.sso.JwtUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 以邮箱验证码的方式登录
 * */
@Component
public class EmailCodeLoginOperation implements LoginOperation{

    @Resource
    UserMapper userMapper;

    @Resource
    RedisUtil redisUtil;

    /**
     * 1. 获取用户信息
     * 2. 验证用户信息
     * 3. 生成token
     * 4. 返回token
     * */
    @Override
    public ResultModel<String> login(UserLoginDto userLoginDto,String ip) {
        if(!userLoginDto.getUserEmailCode().equals(
                getLoginEmailCodeFromCache(userLoginDto.getUserEmail()))){
            return ResultModel.buildFailResultModel("验证码错误！");
        }
        Optional<UserEntity> userEntityOptional = Optional.ofNullable(userMapper.selectOne(
                new QueryWrapper<UserEntity>().eq("user_email", userLoginDto.getUserEmail())
        ));
        if(!userEntityOptional.isPresent()){
            throw new UserNotFoundException("This email is not found,but exists its email code!");
        }
        ResultModel<String> resultModel = ResultModel.<String>builder()
                .data(JwtUtil.generateToken(userEntityOptional.get().entity2Dto(), ip))
                .message("登录成功！")
                .resultFlag(ResultFlag.SUCCESS)
                .resultCode(ResultFlag.SUCCESS.code)
                .build();
        deleteLoginEmailCodeFromCache(userLoginDto.getUserEmail());
        return resultModel;
    }

    /**
     * 从缓存中获取用户的邮箱登录验证码
     * */
    public String getLoginEmailCodeFromCache(String userEmail){
        if(userEmail == null){
            return null;
        }
        return redisUtil.get("REGISTER_EMAIL_CODE_OF_".concat(userEmail));
    }

    /**
     * 从缓存中删除用户的邮箱登录验证码
     * */
    public void deleteLoginEmailCodeFromCache(String userEmail){
        if(userEmail != null){
            redisUtil.delete("REGISTER_EMAIL_CODE_OF_".concat(userEmail));
        }
    }

}
