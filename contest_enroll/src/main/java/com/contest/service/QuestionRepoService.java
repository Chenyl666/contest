package com.contest.service;

import com.contest.dto.question.QuestionRepoDto;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "contest-question")
public interface QuestionRepoService {
    @GetMapping("/contest/question/repo/msg/{question_repo_id}")
    public ResultModel<QuestionRepoDto> getQuestionRepoMessageById(@PathVariable("question_repo_id") Long questionRepoId);
}