package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import com.contest.service.ContestInitService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/contest/init")
public class ContestInitController {

    @Resource
    private ContestInitService contestInitService;

    /**
     * 初始化比赛
     * */
    @GetMapping("/{contest_id}")
    public ResultModel<String> initContest(
            @CurrentUser UserDto userDto,
            @PathVariable("contest_id") Long contestId){
        return contestInitService.initContest(userDto, contestId);
    }

    /**
     * 比赛权限控制
     * */
    @PostMapping("/check")
    public ResultModel<String> checkStatus(
            @CurrentUser UserDto userDto,
            @RequestParam("contestId")Long contestId){
        return contestInitService.checkContestStatus(userDto, contestId);
    }
}
