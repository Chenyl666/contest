package com.contest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.contest.dto.question.QuestionRepoDto;
import com.contest.dto.question.QuestionTagDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.question.QuestionRepoEntity;
import com.contest.entity.question.QuestionTagEntity;
import com.contest.mapper.QuestionRepoMapper;
import com.contest.result.ResultModel;
import com.contest.service.QuestionRepoService;
import com.contest.service.QuestionTagService;
import com.contest.util.SnowMaker;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionRepoServiceImpl extends ServiceImpl<QuestionRepoMapper, QuestionRepoEntity> implements QuestionRepoService {

    private static final SnowMaker snowMaker = new SnowMaker(1);

    @Resource
    private QuestionRepoMapper questionRepoMapper;

    @Resource
    private QuestionTagService questionTagService;

    /**
     * 获取题库
     * */
    @Override
    public ResultModel<List<QuestionRepoDto>> getQuestionRepoDtoByIdAndTagId(String questionTagId) {
        List<QuestionRepoEntity> questionRepoEntityList = list(
                new QueryWrapper<QuestionRepoEntity>().eq("question_tag_id", Long.parseLong(questionTagId))
        );
        List<QuestionRepoDto> questionRepoDtoList = questionRepoEntityList.stream().map(
                QuestionRepoEntity::entity2Dto).collect(Collectors.toList()
        );
        return ResultModel.buildSuccessResultModel(null,questionRepoDtoList);
    }

    /**
     * 添加题库
     * */
    @Override
    public ResultModel<String> addQuestionRepo(UserDto userDto, QuestionRepoDto questionRepoDto) {
        questionRepoDto.setQuestionRepoId(String.valueOf(snowMaker.nextId()));
        QuestionRepoEntity questionRepoEntity = questionRepoDto.dto2Entity();
        supplementQuestionRepoEntity(questionRepoEntity,userDto);
        Optional<QuestionRepoEntity> questionRepoEntityOptional = Optional.ofNullable(getOne(
                new QueryWrapper<QuestionRepoEntity>()
                        .eq("question_repo_name", questionRepoEntity.getQuestionRepoName())
                        .eq("question_tag_id", questionRepoEntity.getQuestionTagId())
                        .eq("created_by", userDto.getUserId())
        ));
        if(!questionRepoEntityOptional.isPresent()){
            save(questionRepoEntity);
            return ResultModel.buildSuccessResultModel(null,String.valueOf(questionRepoEntity.getQuestionRepoId()));
        }
        return ResultModel.buildFailResultModel("题库已存在！");
    }

    /**
     * 删除题库
     * */
    @Override
    public ResultModel<String> deleteQuestionRepoById(String questionRepoId) {
        removeById(Long.parseLong(questionRepoId));
        return ResultModel.buildSuccessResultModel();
    }

    /**
     * 获取题库信息
     * */
    @Override
    public ResultModel<QuestionRepoDto> getQuestionRepoById(Long questionRepoId) {
        ResultModel<QuestionRepoDto> result = null;
        Optional<QuestionRepoEntity> questionRepoEntityOptional = Optional.ofNullable(getById(questionRepoId));
        if (questionRepoEntityOptional.isPresent()) {
            return ResultModel.buildSuccessResultModel(null,questionRepoEntityOptional.get().entity2Dto());
        }
        return ResultModel.buildFailResultModel("no exist",null);
    }

    /**
     * 修改题库名称
     * */
    @Override
    public ResultModel<String> updateQuestionRepoNameById(QuestionRepoDto questionRepoDto) {
        QuestionRepoEntity questionRepoEntity = questionRepoDto.dto2Entity();
        updateById(questionRepoEntity);
        return ResultModel.buildSuccessResultModel();
    }

    /**
     * 获取题库列表
     * */
    @Override
    public ResultModel<Object> getQuestionRepoList(UserDto userDto) {
        List<QuestionRepoDto> questionRepoDtoList = questionRepoMapper.selectList(
                new QueryWrapper<QuestionRepoEntity>().eq("created_By", userDto.getUserId())
        ).stream().map(QuestionRepoEntity::entity2Dto).collect(Collectors.toList());

        List<QuestionTagDto> questionTagDtoList = questionTagService.list(
                new QueryWrapper<QuestionTagEntity>().eq("created_by", userDto.getUserId())
        ).stream().map(QuestionTagEntity::entity2Dto).collect(Collectors.toList());

        List<Object> result = new LinkedList<>();
        questionTagDtoList.forEach(questionTagDto -> {
            Map<String,Object> questionTag = new HashMap<>();
            questionTag.put("tagId",questionTagDto.getQuestionTagId());
            questionTag.put("tagName",questionTagDto.getQuestionTagName());
            LinkedList<Object> questionRepoList = new LinkedList<>();
            questionRepoDtoList.forEach(questionRepoDto -> {
                if(questionRepoDto.getQuestionTagId().equals(questionTagDto.getQuestionTagId())){
                    questionRepoList.add(questionRepoDto);
                }
            });
            questionTag.put("questionRepoList",questionRepoList);
            result.add(questionTag);
        });
        return ResultModel.buildSuccessResultModel(null,result);
    }

    public void supplementQuestionRepoEntity(QuestionRepoEntity questionRepoEntity,UserDto userDto){
        questionRepoEntity.setQuestionRepoId(snowMaker.nextId());
        questionRepoEntity.setCreatedBy(userDto.getUserId());
        questionRepoEntity.setCreatedDate(new Date());
        questionRepoEntity.setUpdatedDate(new Date());
    }
}
