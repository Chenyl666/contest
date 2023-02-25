package com.contest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.contest.dto.question.QuestionRepoDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.question.QuestionRepoEntity;
import com.contest.mapper.QuestionRepoMapper;
import com.contest.result.ResultModel;
import com.contest.service.QuestionRepoService;
import com.contest.util.SnowMaker;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionRepoServiceImpl extends ServiceImpl<QuestionRepoMapper, QuestionRepoEntity> implements QuestionRepoService {

    private static final SnowMaker snowMaker = new SnowMaker(1);

    @Resource
    private QuestionRepoMapper questionRepoMapper;

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

    @Override
    public ResultModel<String> deleteQuestionRepoById(String questionRepoId) {
        removeById(Long.parseLong(questionRepoId));
        return ResultModel.buildSuccessResultModel();
    }

    public void supplementQuestionRepoEntity(QuestionRepoEntity questionRepoEntity,UserDto userDto){
        questionRepoEntity.setQuestionRepoId(snowMaker.nextId());
        questionRepoEntity.setCreatedBy(userDto.getUserId());
        questionRepoEntity.setCreatedDate(new Date());
        questionRepoEntity.setUpdatedDate(new Date());
    }
}
