package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.online.ContestAnswerDto;
import com.contest.dto.online.ContestProgramDto;
import com.contest.dto.user.UserDto;
import com.contest.enu.QuestionType;
import com.contest.result.ResultModel;
import com.contest.service.ContestAnswerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contest/online")
public class ContestOnlineController {

    @Resource
    private ContestAnswerService contestAnswerService;

    /**
     * 获取常规题目的题目和回答情况
     * */
    @GetMapping("/answer/paper/{contest_id}")
    public ResultModel<Map<QuestionType, List<ContestAnswerDto>>> getContestAnswers(
            @CurrentUser UserDto userDto,
            @PathVariable("contest_id")Long contestId){
        return contestAnswerService.getContestAnswers(userDto,contestId);
    }

    /**
     * 获取编程题目的题目和回答情况
     * */
    @GetMapping("/answer/program/{contest_id}")
    public ResultModel<List<ContestProgramDto>> getContestPrograms(
            @CurrentUser UserDto userDto,
            @PathVariable("contest_id") Long contestId){
        return contestAnswerService.getContestPrograms(userDto, contestId);
    }

}
