package com.contest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.contest.dto.question.QuestionDetailDto;
import com.contest.dto.question.QuestionIndexDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.question.QuestionDetailEntity;
import com.contest.enu.QuestionType;
import com.contest.result.ResultModel;

import java.util.List;
import java.util.Map;

public interface QuestionService extends IService<QuestionDetailEntity> {

    /**
     * 保存题目
     * */
    public ResultModel<String> saveQuestion(QuestionDetailDto questionDetailDto, UserDto userDto);

    /**
     * 获取题库的题目索引
     * */
    public ResultModel<Map<String, List<QuestionIndexDto>>> getQuestionIndexByQuestionRepoId(String questionRepoId);

    /**
     * 通过id获取题目
     * */
    public ResultModel<QuestionDetailDto> getQuestionById(String questionId);

    /**
     * 通过id删除题目
     * */
    public ResultModel<String> deleteQuestionById(Long questionId,UserDto userDto);

    /**
     * 获取编程题目录
     * */
    public ResultModel<Map<QuestionType,List<QuestionIndexDto>>> getProgramQuestionIndexById(Long questionRepoId);
}
