package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.question.*;
import com.contest.dto.user.UserDto;
import com.contest.enu.QuestionType;
import com.contest.result.ResultModel;
import com.contest.service.*;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contest/question")
public class QuestionController {

    @Resource
    private QuestionTagService questionTagService;

    @Resource
    private QuestionRepoService questionRepoService;

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionProgramService questionProgramService;

    @Resource
    private ProgramExampleService programExampleService;

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
     * 获取题库有关信息
     * */
    @GetMapping("/repo/msg/{question_repo_id}")
    public ResultModel<QuestionRepoDto> getQuestionRepoMessageById(
            @PathVariable("question_repo_id") Long questionRepoId){
        return questionRepoService.getQuestionRepoById(questionRepoId);
    }

    /**
     * 删除题库
     * */
    @DeleteMapping("/repo/delete/{question_repo_id}")
    public ResultModel<String> deleteQuestionRepo(@PathVariable("question_repo_id") String questionRepoId){
        return questionRepoService.deleteQuestionRepoById(questionRepoId);
    }

    /**
     * 修改题库名称
     * */
    @PostMapping("/repo/update/name")
    public ResultModel<String> updateQuestionRepoNameById(@RequestBody QuestionRepoDto questionRepoDto){
        return questionRepoService.updateQuestionRepoNameById(questionRepoDto);
    }

    /**
     * 保存题目
     * */
    @PostMapping("/save")
    public ResultModel<String> saveQuestion(
            @RequestBody QuestionDetailDto questionDetailDto,
            @CurrentUser UserDto userDto
    ){
        return questionService.saveQuestion(questionDetailDto,userDto);
    }

    /**
     * 获取题库中的所有题目
     * */
    @GetMapping("/index/{question_repo_id}")
    public ResultModel<Map<String, List<QuestionIndexDto>>> getQuestionByQuestionRepoId(
            @PathVariable("question_repo_id")String questionRepoId){
        return questionService.getQuestionIndexByQuestionRepoId(questionRepoId);
    }

    /**
     * 通过id获取题目
     * */
    @GetMapping("/{question_id}")
    public ResultModel<QuestionDetailDto> getQuestionById(@PathVariable("question_id") String questionId){
        return questionService.getQuestionById(questionId);
    }

    /**
     * 通过id删除题目
     * */
    @DeleteMapping("/delete/{question_id}")
    public ResultModel<String> deleteQuestionById(
            @PathVariable("question_id")Long questionId,
            @CurrentUser UserDto userDto){
        return questionService.deleteQuestionById(questionId,userDto);
    }

    /**
     * 保存编程题
     * */
    @PostMapping("/program/save")
    public ResultModel<String> saveQuestionProgram(
            @RequestBody QuestionProgramDto questionProgramDto,
            @CurrentUser UserDto userDto){
        return questionProgramService.saveQuestionProgram(questionProgramDto,userDto);
    }

    /**
     * 获取编程题目录
     * */
    @GetMapping("/program/index/{question_repo_id}")
    public ResultModel<Map<QuestionType,List<QuestionIndexDto>>> getProgramQuestionIndex(
            @PathVariable("question_repo_id") Long questionRepoId){
        return questionService.getProgramQuestionIndexById(questionRepoId);
    }

    /**
     * 通过id获取编程题
     * */
    @GetMapping("/program/get/{question_id}")
    public ResultModel<QuestionProgramDto> getQuestionProgramById(@PathVariable("question_id") Long questionId){
        return questionProgramService.getQuestionProgramById(questionId);
    }

    /**
     * 删除测试用例
     * */
    @PostMapping("/program/example/delete")
    public ResultModel<String> deleteProgramExample(
            @RequestBody Map<String,Long> params,
            @CurrentUser UserDto userDto){
        return programExampleService.deleteByQuestionIdAndExampleNumber(
                params.get("questionId"),
                Integer.parseInt(params.get("exampleNumber").toString()),
                userDto
        );
    }

    /**
     * 获取题库列表
     * */
    @GetMapping("/repo/list")
    public ResultModel<Object> getQuestionRepoList(@CurrentUser UserDto userDto){
        return questionRepoService.getQuestionRepoList(userDto);
    }
}
