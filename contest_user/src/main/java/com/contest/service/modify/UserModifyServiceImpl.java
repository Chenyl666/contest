package com.contest.service.modify;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.contest.async.provider.sender.ModifyCodeProvider;
import com.contest.util.RedisUtil;
import com.contest.dto.ModifyPwdDto;
import com.contest.dto.notify.EmailCodeMessageDto;
import com.contest.entity.user.UserEntity;
import com.contest.mapper.UserMapper;
import com.contest.result.ResultModel;
import com.contest.util.RandomGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class UserModifyServiceImpl implements UserModifyService{
    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ModifyCodeProvider modifyCodeProvider;

    private static final String CACHE_KEY_PREFIX = "MODIFY_PASSWORD";

    /**
     * 发送修改密码时的邮箱验证码
     * 1. 验证邮箱是否已经被注册过
     * 2. 生成四位随机验证码（1成立）
     * 3. 将验证码存入缓存中（1成立）
     * 4. 将验证码发送到用户邮箱中（1成立）
     * */
    @Override
    public ResultModel<String> sendModifyPasswordEmailCode(String userEmail) {
        Optional<UserEntity> userOptional = Optional.ofNullable(userMapper.selectOne(
                new QueryWrapper<UserEntity>().eq("user_email", userEmail)
        ));
        if(!userOptional.isPresent()){
            return ResultModel.buildFailResultModel("邮箱未注册！");
        }
        String emailCode = RandomGenerator.generateRandomEmailCode();
        setModifyPasswordEmailCodeIntoCache(userEmail,emailCode);
        modifyCodeProvider.sendModifyPasswordEmailCode(
                EmailCodeMessageDto.builder()
                        .code(emailCode)
                        .userEmail(userEmail)
                        .build()
        );
        return ResultModel.buildSuccessResultModel("验证码发送成功！");
    }

    /**
     * 保存修改密码信息
     * 1. 检查验证码是否在缓存中
     * 2. 将修改密码的信息保存到数据库中
     * */
    @Override
    public ResultModel<String> saveModifyPassword(ModifyPwdDto modifyPwdDto) {
        if(!modifyPwdDto.getEmailCode().equals(getModifyPasswordEmailCodeFromCache(modifyPwdDto.getUserEmail()))){
            return ResultModel.buildFailResultModel("验证码错误！");
        }
        deleteModifyPasswordEmailCodeFromCache(modifyPwdDto.getUserEmail());
        boolean result = userMapper.updateUserPwdByUserEmail(modifyPwdDto);
        if(!result){
            return ResultModel.buildFailResultModel("邮箱未被注册！");
        }
        return ResultModel.buildSuccessResultModel("修改密码成功！");
    }

    /**
     * 将验证码存入缓存中
     * */
    private void setModifyPasswordEmailCodeIntoCache(String userEmail,String emailCode){
        redisUtil.set(CACHE_KEY_PREFIX.concat(userEmail),emailCode,5, TimeUnit.MINUTES);
    }

    /**
     * 从缓存中获取验证码
     * */
    private String getModifyPasswordEmailCodeFromCache(String userEmail){
        return redisUtil.get(CACHE_KEY_PREFIX.concat(userEmail));
    }

    /**
     * 删除缓存中的验证码
     * */
    private void deleteModifyPasswordEmailCodeFromCache(String userEmail){
        redisUtil.delete(CACHE_KEY_PREFIX.concat(userEmail));
    }
}
