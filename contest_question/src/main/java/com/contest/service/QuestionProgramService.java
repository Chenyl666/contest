package com.contest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.contest.dto.question.QuestionProgramDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.question.QuestionProgramEntity;
import com.contest.result.ResultModel;

public interface QuestionProgramService extends IService<QuestionProgramEntity> {
    /**
     * 保存编程题
     * */
    public ResultModel<String> saveQuestionProgram(QuestionProgramDto questionProgramDto, UserDto userDto);

    /**
     * 获取编程题
     * */
    public ResultModel<QuestionProgramDto> getQuestionProgramById(Long questionId);

    /**
     * 删除编程题
     * */
    public ResultModel<String> deleteQuestionProgramById(Long questionId,UserDto userDto);
}
