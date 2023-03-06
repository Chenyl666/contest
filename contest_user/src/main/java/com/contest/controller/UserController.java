package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.user.UserDetailDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.user.UserEntity;
import com.contest.result.ResultModel;
import com.contest.service.user.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 通过id获取用户对象
     * */
    @GetMapping("/get/{user_id}")
    public ResultModel<UserDto> getUserById(@PathVariable("user_id")String userId){
        Optional<UserEntity> userEntityOptional = Optional.ofNullable(userService.getById(userId));
        if(userEntityOptional.isPresent()){
            return ResultModel.buildSuccessResultModel(null,userEntityOptional.get().entity2Dto());
        }
        return ResultModel.buildFailResultModel("no",null);
    }

    /**
     * 获取用户详细信息
     * */
    @GetMapping("/get/detail")
    public ResultModel<UserDetailDto> getUserDetail(@CurrentUser UserDto userDto){
        return userService.getUserDetailDto(userDto);
    }

    /**
     * 保存用户信息
     * */
    @PostMapping("/save")
    public ResultModel<String> saveUser(@RequestBody UserDetailDto userDetailDto){
        return userService.saveUser(userDetailDto);
    }

}
