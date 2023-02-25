package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.question.QuestionRepoDto;
import com.contest.dto.question.QuestionTagDto;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import com.contest.service.QuestionRepoService;
import com.contest.service.QuestionTagService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/contest/question")
public class QuestionController {

    @Resource
    private QuestionTagService questionTagService;

    @Resource
    private QuestionRepoService questionRepoService;

    /**
     * 添加题库书签
     * */
    @PostMapping("/tag/add")
    public ResultModel<String> createQuestionTag(
            @RequestBody QuestionTagDto questionTagDto,
            @CurrentUser UserDto userDto
    ){
        return questionTagService.createQuestionTag(questionTagDto,userDto);
    }

    /**
     * 查询书签
     * */
    @GetMapping("/tag/get")
    public ResultModel<List<QuestionTagDto>> getQuestionTagList(@CurrentUser UserDto userDto){
        return questionTagService.getQuestionTagList(userDto);
    }

    /**
     * 删除书签
     * */
    @DeleteMapping("/tag/delete/{question_tag_id}")
    public ResultModel<String> deleteQuestionTag(@PathVariable("question_tag_id") String questionTagId){
        return questionTagService.deleteQuestionTag(questionTagId);
    }

    /**
     * 添加题库
     * */
    @PostMapping("/repo/add")
    public ResultModel<String> createQuestionRepo(
            @CurrentUser UserDto userDto,
            @RequestBody QuestionRepoDto questionRepoDto){
        return questionRepoService.addQuestionRepo(userDto,questionRepoDto);
    }

    /**
     * 获取题库
     * */
    @GetMapping("/repo/get/{tag_id}")
    public ResultModel<List<QuestionRepoDto>> getQuestionRepo(@PathVariable("tag_id")String questionTagId){
        return questionRepoService.getQuestionRepoDtoByIdAndTagId(questionTagId);
    }

    /**
     * 删除题库
     * */
    @DeleteMapping("/repo/delete/{question_repo_id}")
    public ResultModel<String> deleteQuestionRepo(@PathVariable("question_repo_id") String questionRepoId){
        return questionRepoService.deleteQuestionRepoById(questionRepoId);
    }



}
