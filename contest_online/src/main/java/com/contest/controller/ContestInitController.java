package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import com.contest.service.ContestInitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
