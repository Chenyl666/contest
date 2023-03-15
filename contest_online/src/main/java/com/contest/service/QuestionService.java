package com.contest.service;

import com.contest.dto.question.QuestionDetailDto;
import com.contest.dto.question.QuestionIndexDto;
import com.contest.dto.question.QuestionProgramDto;
import com.contest.result.ResultModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@FeignClient("contest-question")
public interface QuestionService {
    @GetMapping("/contest/question/list/{question_repo_id}")
    public ResultModel<List<QuestionIndexDto>> getQuestionsByRepoId(
            @PathVariable("question_repo_id") Long questionRepoId
    );

    @GetMapping("/contest/question/list/paper/{question_repo_id}")
    public ResultModel<List<QuestionDetailDto>> getQuestionListByRepoId(
            @PathVariable("question_repo_id")Long questionRepoId
    );

    @GetMapping("/contest/question/list/program/{question_repo_id}")
    public ResultModel<List<QuestionProgramDto>> getQuestionProgramListByRepoId(
            @PathVariable("question_repo_id")Long questionRepoId
    );

    @GetMapping("/contest/question/program/get/{question_id}")
    public ResultModel<QuestionProgramDto> getQuestionProgramById(
            @PathVariable("question_id") Long questionId
    );

    @GetMapping("/contest/question/list2/{question_repo_id}")
    public ResultModel<List<QuestionDetailDto>> getQuestionDetailDtoListByRepoId(@PathVariable("question_repo_id") Long questionRepoId);
}
