package com.contest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.contest.dto.contest.ContestAnswerResultDto;
import com.contest.dto.online.ContestAnswerDto;
import com.contest.dto.online.ContestProgramDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.online.ContestAnswerEntity;
import com.contest.enu.QuestionType;
import com.contest.result.ResultModel;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

public interface ContestAnswerService extends IService<ContestAnswerEntity> {

    /**
     * 获取常规题目的回答情况
     * */
    public ResultModel<Map<QuestionType, List<ContestAnswerDto>>> getContestAnswers(UserDto userDto, Long contestId);

    /**
     * 获取编程题目的回答情况
     * */
    public ResultModel<List<ContestProgramDto>> getContestPrograms(UserDto userDto,Long contestId);

//    /**
//     * 保存常规题目
//     * */
//    public ResultModel<String> savePaperQuestionAnswer(ContestAnswerDto contestAnswerDto);

    /**
     * 保存常规题目
     * */
    public ResultModel<String> save(Map<QuestionType,List<ContestAnswerDto>> contestAnswerDtoMap);

    /**
     * 设置用户的比赛状态
     * */
    public ResultModel<String> updateUserContestStatus(UserDto userDto,Long contestId,Integer status);

    /**
     * 获取填空题和问答题的回答情况
     * */
    public ResultModel<List<ContestAnswerResultDto>> getSupplementAndAnswerQuestionList(Long contestId);

    /**
     * 手动判题
     * */
    public ResultModel<String> judgeByEnrollId(Long answerId,Float score);

    /**
     * 自动判题
     * */
    public ResultModel<String> judgeAutoByContestId(Long contestId);
}
