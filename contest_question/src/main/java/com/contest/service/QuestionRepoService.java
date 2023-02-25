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

    public ResultModel<List<QuestionRepoDto>> getQuestionRepoDtoByIdAndTagId(String questionTagId);

    public ResultModel<String> addQuestionRepo(UserDto userDto,QuestionRepoDto questionRepoDto);

    public ResultModel<String> deleteQuestionRepoById(String questionRepoId);

}
