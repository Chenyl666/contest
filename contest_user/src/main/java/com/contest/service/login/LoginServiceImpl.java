package com.contest.service.login;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.contest.async.provider.sender.LoginCodeProvider;
import com.contest.sso.JwtUtil;
import com.contest.util.RedisUtil;
import com.contest.dto.notify.EmailCodeMessageDto;
import com.contest.dto.user.UserLoginDto;
import com.contest.entity.user.UserEntity;
import com.contest.mapper.UserMapper;
import com.contest.result.ResultModel;
import com.contest.service.login.auth.LoginOperation;
import com.contest.util.RandomGenerator;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    UserMapper userMapper;

    @Resource
    LoginCodeProvider loginCodeProvider;

    @Resource
    RedisUtil redisUtil;

    @Resource(name = "loginOperationMap")
    Map<Integer, LoginOperation> loginServiceMap;

    /**
     * 发送邮箱登录验证码
     * 1. 检查userEmail是否被注册
     * 2. 生成验证码 (1成立)
     * 3. 将验证码保存到缓存重 (1成立)
     * 4. 发送验证码到用户邮箱 (1成立)
     * */
    @Override
    public ResultModel<String> sendUserLoginEmailCode(String userEmail) {
        Optional<UserEntity> userEntityOptional = Optional.ofNullable(userMapper.selectOne(new QueryWrapper<UserEntity>().eq("user_email", userEmail)));
        userEntityOptional.ifPresent(userEntity -> {
            EmailCodeMessageDto emailCodeMessageDto = EmailCodeMessageDto.builder()
                    .userEmail(userEmail)
                    .code(RandomGenerator.generateRandomEmailCode())
                    .build();
            setEmailCodeIntoCache(emailCodeMessageDto);
            loginCodeProvider.sendLoginEmailCode(emailCodeMessageDto);
        });
        return userEntityOptional.isPresent()
                ? ResultModel.buildSuccessResultModel("验证码发送成功！")
                : ResultModel.buildFailResultModel("该用户未注册！");
    }

    /**
     * 用户登录
     * */
    @Override
    public ResultModel<String> login(UserLoginDto userLoginDto,String ip) {
        return loginServiceMap.get(userLoginDto.getLoginOperation()).login(userLoginDto,ip);
    }

    /**
     * 鉴定token
     * */
    @Override
    public ResultModel<String> authToken(String token,String ipAddr) {
        if(JwtUtil.verify(token)){
            token = JwtUtil.generateToken(token, ipAddr);
            if(token != null){
                return ResultModel.buildSuccessResultModel("",token);
            }
        }
        return ResultModel.buildFailResultModel("鉴定失败！");
    }

    /**
     * 将邮箱登录验证码放入缓存
     * */
    public void setEmailCodeIntoCache(EmailCodeMessageDto emailCodeMessageDto){
        redisUtil.set(
                "REGISTER_EMAIL_CODE_OF_".concat(emailCodeMessageDto.getUserEmail())
                , emailCodeMessageDto.getCode()
                , 300
                , TimeUnit.SECONDS
        );
    }

}
