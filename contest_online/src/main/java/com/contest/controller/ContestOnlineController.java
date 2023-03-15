package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.contest.ContestAnswerResultDto;
import com.contest.dto.online.ContestAnswerDto;
import com.contest.dto.online.ContestProgramDto;
import com.contest.dto.user.UserDto;
import com.contest.enu.QuestionType;
import com.contest.result.ResultModel;
import com.contest.service.ContestAnswerService;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 保存所有常规题目的回答情况
     * */
    @PostMapping("/answer/paper/save")
    public ResultModel<String> savePaperAnswer(@RequestBody Map<QuestionType,List<ContestAnswerDto>> contestAnswerDtoMap){
        return contestAnswerService.save(contestAnswerDtoMap);
    }

    /**
     * 更新用户的竞赛状态
     * */
    @PostMapping("/status/update")
    public ResultModel<String> updateUserContestStatus(
            @RequestParam("status")Integer status,
            @CurrentUser UserDto userDto,
            @RequestParam("contestId")Long contestId
    ){
        return contestAnswerService.updateUserContestStatus(userDto, contestId, status);
    }

    /**
     * 获取考试的所有填空题和问答题
     * */
    @GetMapping("/question/judge/{contest_id}")
    public ResultModel<List<ContestAnswerResultDto>> getSupplementAndAnswerQuestionList(@PathVariable("contest_id")Long contestId){
        return contestAnswerService.getSupplementAndAnswerQuestionList(contestId);
    }

    /**
     * 手动判题
     * */
    @PostMapping("/judge")
    public ResultModel<String> judgeByEnrollId(@RequestParam("answerId")Long answerId,@RequestParam("score")Float score){
        return contestAnswerService.judgeByEnrollId(answerId, score);
    }

    /**
     * 自动判题
     * */
    @PostMapping("/judge/auto")
    public ResultModel<String> judgeAutoByContestId(@RequestParam("contestId")Long contestId){
        return contestAnswerService.judgeAutoByContestId(contestId);
    }
}
