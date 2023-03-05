package com.contest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.question.QuestionRepoDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.question.QuestionRepoEntity;
import com.contest.result.ResultModel;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface QuestionRepoService extends IService<QuestionRepoEntity> {

    /**
     * 获取题库
     * */
    public ResultModel<List<QuestionRepoDto>> getQuestionRepoDtoByIdAndTagId(String questionTagId);

    /**
     * 添加题库
     * */
    public ResultModel<String> addQuestionRepo(UserDto userDto,QuestionRepoDto questionRepoDto);

    /**
     * 删除题库
     * */
    public ResultModel<String> deleteQuestionRepoById(String questionRepoId);

    /**
     * 获取题库信息
     * */
    public ResultModel<QuestionRepoDto> getQuestionRepoById(Long questionRepoId);

    /**
     * 修改题库名称
     * */
    public ResultModel<String> updateQuestionRepoNameById(QuestionRepoDto questionRepoDto);

}
