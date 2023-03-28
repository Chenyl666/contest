package com.contest.service;

import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@FeignClient("contest-user")
public interface UserService {
    @GetMapping("/user/get/list/{ids}")
    public ResultModel<List<UserDto>> getUserDtoListByUserIdList(@PathVariable("ids") String idsStr);
}
