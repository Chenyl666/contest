package com.contest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.contest.dto.question.QuestionTagDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.question.QuestionTagEntity;
import com.contest.result.ResultModel;

import java.util.List;

public interface QuestionTagService extends IService<QuestionTagEntity> {
    public ResultModel<String> createQuestionTag(QuestionTagDto questionTagDto, UserDto userDto);

    public ResultModel<List<QuestionTagDto>> getQuestionTagList(UserDto userDto);
}
