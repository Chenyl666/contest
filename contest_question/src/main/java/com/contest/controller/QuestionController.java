package com.contest.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.question.QuestionTagDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.question.QuestionTagEntity;
import com.contest.result.ResultModel;
import com.contest.service.QuestionTagService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/contest/question")
public class QuestionController {

    @Resource
    private QuestionTagService questionTagService;

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

}
