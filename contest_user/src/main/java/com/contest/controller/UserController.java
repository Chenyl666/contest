package com.contest.controller;

import com.contest.dto.user.UserDto;
import com.contest.entity.user.UserEntity;
import com.contest.result.ResultModel;
import com.contest.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
