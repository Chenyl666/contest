package com.contest.service.register;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.contest.async.provider.sender.RegisterCodeProvider;
import com.contest.result.util.RedisUtil;
import com.contest.dto.notify.EmailCodeMessageDto;
import com.contest.dto.user.UserRegisterDto;
import com.contest.entity.user.UserEntity;
import com.contest.mapper.UserMapper;
import com.contest.result.ResultFlag;
import com.contest.result.ResultModel;
import com.contest.result.util.RandomGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Resource
    RegisterCodeProvider registerCodeProvider;

    @Resource
    RedisUtil redisUtil;

    @Value("${user.default.pic}")
    String defaultUserPicUrl;

    @Resource
    UserMapper userMapper;

    /**
     * 发送邮箱注册验证码
     * 1. 随机生成四位验证码
     * 2. 将验证码保存到缓存中
     * 3. 调用notify服务将验证码发送到用户邮箱（异步）
     * */
    @Override
    public ResultModel<String> sendRegisterEmailCode(String userEmail) {
        String emailCode = RandomGenerator.generateRandomEmailCode();
        setEmailCodeIntoCache(userEmail,emailCode);
        registerCodeProvider.sendRegisterEmailCode(
                EmailCodeMessageDto.builder()
                        .code(emailCode)
                        .userEmail(userEmail)
                        .build()
        );
        return ResultModel.<String>builder()
                .resultFlag(ResultFlag.SUCCESS)
                .resultCode(ResultFlag.SUCCESS.code)
                .message(ResultFlag.SUCCESS.msg)
                .build();
    }

    /**
     * 保存用户注册信息
     * 1. 确认验证码的有效性
     * 2. 将用户信息保存到数据库
     * 3. 删除缓存中的验证码（成功将用户信息保存到数据库的情况）
     * */
    @Override
    public ResultModel<String> registerUser(UserRegisterDto userRegisterDto) {
        if(!userRegisterDto.getEmailCode().equals(getEmailCodeFromCache(userRegisterDto.getUserEmail()))){
            return ResultModel.buildFailResultModel("验证码无效！");
        }
        try{
            if(userMapper.insert(convertAndSupplementUserEntity(userRegisterDto)) == 0){
                throw new Exception("User".concat(userRegisterDto.getUserId()).concat("has been registered!"));
            }
        } catch (Exception e){
            return ResultModel.buildFailResultModel("该用户已被注册！");
        }
        deleteEmailCodeFromCache(userRegisterDto.getUserEmail());
        return ResultModel.buildSuccessResultModel("注册成功！");
    }

    /**
     * 检查邮箱是否已经被注册
     * */
    @Override
    public ResultModel<String> checkingEmailExistOrNot(String userEmail) {
        Optional<UserEntity> userEntityOptional = Optional.ofNullable(
                userMapper.selectOne(new QueryWrapper<UserEntity>().eq("user_email", userEmail))
        );
        if(!userEntityOptional.isPresent()){
            return ResultModel.buildSuccessResultModel("邮箱未被注册！");
        }
        return ResultModel.buildFailResultModel("邮箱已经被注册！");
    }

    /**
     * 检查用户id是否已经被注册
     * */
    @Override
    public ResultModel<String> checkingUserIdExistOrNot(String userId) {
        Optional<UserEntity> userEntityOptional = Optional.ofNullable(
                userMapper.selectOne(new QueryWrapper<UserEntity>().eq("user_id", userId))
        );
        if(userEntityOptional.isPresent()){
            return ResultModel.buildFailResultModel("该用户id已经被注册！");
        }
        return ResultModel.buildSuccessResultModel("该用户未被注册！");
    }

    /**
     * 将邮箱注册验证码保存到缓存中
     * */
    public void setEmailCodeIntoCache(String userEmail, String code){
        redisUtil.set("REGISTER_EMAIL_CODE_OF_".concat(userEmail),code,300, TimeUnit.SECONDS);
    }

    /**
     * 从缓存中取出邮箱注册验证码
     * */
    public String getEmailCodeFromCache(String userEmail){
        return redisUtil.get("REGISTER_EMAIL_CODE_OF_".concat(userEmail));
    }

    /**
     * 从缓存中删除邮箱注册验证码
     * */
    public void deleteEmailCodeFromCache(String userEmail){
        redisUtil.delete("REGISTER_EMAIL_CODE_OF_".concat(userEmail));
    }

    /**
     * 生成UserEntity
     * */
    public UserEntity convertAndSupplementUserEntity(UserRegisterDto userRegisterDto){
        UserEntity userEntity = userRegisterDto.toUserEntity();
        userEntity.setUserPic(this.defaultUserPicUrl);
        userEntity.setCreatedDate(new Date(System.currentTimeMillis()));
        userEntity.setUserName("新用户".concat(userRegisterDto.getUserId()));
        return userEntity;
    }

}
