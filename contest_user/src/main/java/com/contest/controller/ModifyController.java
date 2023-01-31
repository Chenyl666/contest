package com.contest.controller;

import com.contest.dto.ModifyPwdDto;
import com.contest.result.ResultModel;
import com.contest.service.modify.UserModifyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/modify")
public class ModifyController {
    @Resource
    private UserModifyService userModifyService;

    @GetMapping("/email/code/{userEmail}")
    public ResultModel<String> sendModifyPasswordEmailCode(@PathVariable("userEmail")String userEmail){
        return userModifyService.sendModifyPasswordEmailCode(userEmail);
    }

    @PostMapping("/save")
    public ResultModel<String> saveModifyPassword(@RequestBody ModifyPwdDto modifyPwdDto){
        return userModifyService.saveModifyPassword(modifyPwdDto);
    }

}
