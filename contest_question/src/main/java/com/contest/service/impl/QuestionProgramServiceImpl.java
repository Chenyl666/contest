package com.contest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.contest.async.processor.AsyncListener;
import com.contest.dto.question.ProgramExampleDto;
import com.contest.dto.question.QuestionProgramDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.question.ProgramExampleEntity;
import com.contest.entity.question.QuestionProgramEntity;
import com.contest.mapper.QuestionProgramMapper;
import com.contest.result.ResultModel;
import com.contest.service.ProgramExampleService;
import com.contest.service.QuestionProgramService;
import com.contest.util.SnowMaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionProgramServiceImpl extends ServiceImpl<QuestionProgramMapper, QuestionProgramEntity> implements QuestionProgramService {

    @Resource
    private ProgramExampleService programExampleService;

    @Resource
    private AsyncListener asyncListener;

    private static final SnowMaker snowMaker = new SnowMaker(1);

    /**
     * 保存编程题
     * */
    @Override
    @Transactional
    public ResultModel<String> saveQuestionProgram(QuestionProgramDto questionProgramDto, UserDto userDto) {
        if(questionProgramDto.getQuestionId() != null){
            List<ProgramExampleDto> programExampleList = questionProgramDto.getProgramExampleList();
            programExampleService.remove(new QueryWrapper<ProgramExampleEntity>().eq("question_id",questionProgramDto.getQuestionId()));
            programExampleService.saveBatch(
                    programExampleList.stream()
                            .map(ProgramExampleDto::dto2Entity)
                            .collect(Collectors.toList())
            );
            QuestionProgramEntity questionProgramEntity = questionProgramDto.dto2Entity();
            updateById(questionProgramEntity);
        }else{
            questionProgramDto.setQuestionId(String.valueOf(snowMaker.nextId()));
            QuestionProgramEntity questionProgramEntity = questionProgramDto.dto2Entity();
            questionProgramEntity.setUpdatedBy(new Date());
            questionProgramEntity.setCreatedBy(userDto.getUserId());
            save(questionProgramEntity);
            List<ProgramExampleDto> programExampleList = questionProgramDto.getProgramExampleList();
            List<ProgramExampleEntity> programExampleEntityList = programExampleList.stream().map(programExampleDto -> {
                programExampleDto.setQuestionId(questionProgramDto.getQuestionId());
                return programExampleDto.dto2Entity();
            }).collect(Collectors.toList());
            programExampleService.saveBatch(programExampleEntityList);
        }
        for (ProgramExampleDto programExampleDto : questionProgramDto.getProgramExampleList()) {
            String[] split = programExampleDto.getExampleUrl().split("/");
            Long fileId = Long.parseLong(split[split.length-1]);
            asyncListener.setFileNotTimeout(fileId);
        }
        return ResultModel.buildSuccessResultModel();
    }

    /**
     * 获取编程题
     * */
    @Override
    public ResultModel<QuestionProgramDto> getQuestionProgramById(Long questionId) {
        QuestionProgramEntity questionProgramEntity = getById(questionId);
        if(questionProgramEntity != null){
            List<ProgramExampleEntity> programExampleEntityList = programExampleService.list(
                    new QueryWrapper<ProgramExampleEntity>().eq("question_id", questionId)
            );
            QuestionProgramDto questionProgramDto = questionProgramEntity.entity2Dto(programExampleEntityList);
            return ResultModel.buildSuccessResultModel(null,questionProgramDto);
        }
        return ResultModel.buildFailResultModel("not exist!", null);
    }

    /**
     * 删除编程题
     * */
    @Override
    @Transactional
    public ResultModel<String> deleteQuestionProgramById(Long questionId,UserDto userDto) {
        List<ProgramExampleEntity> programExampleEntityList = programExampleService.list(new QueryWrapper<ProgramExampleEntity>().eq("question_id", questionId));
        programExampleEntityList.forEach(programExampleEntity -> {
            programExampleService.deleteByQuestionIdAndExampleNumber(
                    programExampleEntity.getQuestionId(),
                    programExampleEntity.getExampleNumber(),
                    userDto
            );
        });
        removeById(questionId);
        return ResultModel.buildSuccessResultModel();
    }
}
