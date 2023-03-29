package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.contest.ContestResultDto;
import com.contest.dto.contest.ContestUserResult;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import com.contest.service.ContestResultService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/contest/result")
public class ResultController {

    @Resource
    private ContestResultService contestResultService;

    /**
     * 生成成绩
     * */
    @PostMapping("/generate")
    public ResultModel<String> generateContestResult(@RequestParam("contestId")Long contestId){
        return contestResultService.generateContestResult(contestId);
    }

    /**
     * 发布成绩
     * */
    @PostMapping("/publish")
    public ResultModel<String> publishContestResult(@RequestParam("contestId")Long contestId){
        return contestResultService.publishContestResult(contestId);
    }

    /**
     * 获取成绩
     * */
    @GetMapping("/list/{contest_id}")
    public ResultModel<List<ContestResultDto>> getContestResultList(@PathVariable("contest_id")Long contestId){
        return contestResultService.getContestResultListByContestId(contestId);
    }

    /**
     * 导出成绩列表
     * */
    @GetMapping("/excel/{contest_id}")
    public void exportContestResultAsExcel(@PathVariable("contest_id")Long contestId, HttpServletResponse response){
        contestResultService.exportContestResultAsExcel(contestId, response);
    }

    /**
     * 获取用户的竞赛成绩
     * */
    @GetMapping("/get/{contest_id}")
    public ResultModel<ContestUserResult> getContestResultByUserIdAndContestId(
            @PathVariable("contest_id")Long contestId, @CurrentUser UserDto userDto){
        return contestResultService.getContestUserResult(contestId, userDto);
    }
}
