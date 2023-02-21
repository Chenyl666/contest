package com.contest.service;

import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "contest-user")
public interface UserService {
    @GetMapping("/user/get/{user_id}")
    public ResultModel<UserDto> getUserById(@PathVariable("user_id")String userId);
}
