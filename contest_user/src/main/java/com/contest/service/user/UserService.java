package com.contest.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.contest.dto.user.UserDetailDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.user.UserEntity;
import com.contest.result.ResultModel;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserService extends IService<UserEntity> {
    public ResultModel<UserDetailDto> getUserDetailDto(UserDto userDto);

    public ResultModel<String> saveUser(UserDetailDto userDetailDto);

    public ResultModel<UserDetailDto> getUserDetailById(String userId);
}
