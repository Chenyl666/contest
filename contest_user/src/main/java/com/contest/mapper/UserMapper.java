package com.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.contest.dto.ModifyPwdDto;
import com.contest.entity.user.UserEntity;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    @Update("update user set user_pwd = #{userPwd} where user_email = #{userEmail}")
    public boolean updateUserPwdByUserEmail(ModifyPwdDto modifyPwdDto);

}
