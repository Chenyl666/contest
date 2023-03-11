package com.contest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.contest.dto.online.ContestAnswerDto;
import com.contest.dto.online.ContestProgramDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.online.ContestAnswerEntity;
import com.contest.enu.QuestionType;
import com.contest.mapper.ContestAnswerMapper;
import com.contest.result.ResultModel;

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

}
