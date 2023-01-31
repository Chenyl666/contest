package com.contest.controller;

import com.contest.dto.user.UserRegisterDto;
import com.contest.result.ResultModel;
import com.contest.service.register.RegisterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Resource
    private RegisterService registerService;

    /**
     * 发送邮箱注册验证码
     * */
    @GetMapping("/email/code/{userEmail}")
    public ResultModel<String> sendRegisterEmailCode(@PathVariable("userEmail")String userEmail){
        return registerService.sendRegisterEmailCode(userEmail);
    }

    /**
     * 保存用户注册信息
     * */
    @PostMapping("/submit")
    public ResultModel<String> registerUser(@RequestBody UserRegisterDto userRegisterDto){
        return registerService.registerUser(userRegisterDto);
    }

    /**
     * 查看邮箱是否已经被注册
     * */
    @GetMapping("/email/exists/{userEmail}")
    public ResultModel<String> checkingEmailExistsOrNot(@PathVariable("userEmail") String userEmail){
        return registerService.checkingEmailExistOrNot(userEmail);
    }

    /**
     * 查看用户id是否已经被注册
     * */
    @GetMapping("/id/exists/{userId}")
    public ResultModel<String> checkingUserIdExistsOrNot(@PathVariable("userId") String userId){
        return registerService.checkingUserIdExistOrNot(userId);
    }
}
