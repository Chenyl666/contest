package com.contest.controller;

import com.contest.dto.user.UserLoginDto;
import com.contest.result.ResultModel;
import com.contest.service.login.LoginService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Resource
    LoginService loginService;

    /**
     * 发送邮箱登录验证码
     * */
    @GetMapping("/email/code/{userEmail}")
    public ResultModel<String> sendUserLoginEmailCode(@PathVariable("userEmail") String userEmail){
        return loginService.sendUserLoginEmailCode(userEmail);
    }

    /**
     * 登录验证（支持多种方式）
     * */
    @PostMapping("/submit")
    public ResultModel<String> login(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request){
        return loginService.login(userLoginDto,request.getRemoteHost());
    }


}
