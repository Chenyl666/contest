package com.contest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.contest.dto.question.QuestionTagDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.question.QuestionTagEntity;
import com.contest.mapper.QuestionTagMapper;
import com.contest.result.ResultModel;
import com.contest.service.QuestionTagService;
import com.contest.util.SnowMaker;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionTagServiceImpl extends ServiceImpl<QuestionTagMapper, QuestionTagEntity> implements QuestionTagService {

    private static final SnowMaker snowMaker = new SnowMaker(1);

    /**
     * 添加标签
     * */
    @Override
    public ResultModel<String> createQuestionTag(QuestionTagDto questionTagDto, UserDto userDto) {
        questionTagDto.setQuestionTagId(String.valueOf(snowMaker.nextId()));
        QuestionTagEntity questionTagEntity = questionTagDto.dto2Entity();
        questionTagEntity.setCreatedBy(userDto.getUserId());
        questionTagEntity.setCreatedDate(new Date());
        Optional<QuestionTagEntity> questionTagOptional = Optional.ofNullable(this.getOne(
                new QueryWrapper<QuestionTagEntity>().eq(
                        "question_tag_name", questionTagEntity.getQuestionTagName()
                )
        ));
        if(questionTagOptional.isPresent()){
            return ResultModel.buildFailResultModel("标签已存在");
        }
        save(questionTagEntity);
        return ResultModel.buildSuccessResultModel();
    }

    /**
     * 获取标签
     * */
    @Override
    public ResultModel<List<QuestionTagDto>> getQuestionTagList(UserDto userDto) {
        List<QuestionTagEntity> questionTagEntityList = this.list(new QueryWrapper<QuestionTagEntity>().eq("created_by", userDto.getUserId()));
        List<QuestionTagDto> questionTagDtoList = questionTagEntityList.stream().map(QuestionTagEntity::entity2Dto).collect(Collectors.toList());
        return ResultModel.buildSuccessResultModel(null,questionTagDtoList);
    }
}
