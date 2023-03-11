package com.contest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.contest.dto.question.QuestionDetailDto;
import com.contest.dto.question.QuestionIndexDto;
import com.contest.dto.question.QuestionProgramDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.question.QuestionDetailEntity;
import com.contest.entity.question.QuestionProgramEntity;
import com.contest.entity.question.QuestionRepoEntity;
import com.contest.enu.QuestionRepoType;
import com.contest.enu.QuestionType;
import com.contest.mapper.QuestionDetailMapper;
import com.contest.mapper.QuestionProgramMapper;
import com.contest.mapper.QuestionRepoMapper;
import com.contest.result.ResultModel;
import com.contest.service.QuestionProgramService;
import com.contest.service.QuestionService;
import com.contest.util.SnowMaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionDetailMapper, QuestionDetailEntity> implements QuestionService {

    private static final SnowMaker snowMaker = new SnowMaker(1);

    @Resource
    private QuestionProgramMapper questionProgramMapper;

    @Resource
    private QuestionProgramService questionProgramService;

    @Resource
    private QuestionRepoMapper questionRepoMapper;

    /**
     * 保存题目
     * */
    @Override
    public ResultModel<String> saveQuestion(QuestionDetailDto questionDetailDto, UserDto userDto) {
        questionDetailDto.setCreatedBy(userDto.getUserId());
        QuestionDetailEntity questionDetailEntity = null;
        if(questionDetailDto.getQuestionId() == null){
            questionDetailDto.setQuestionId(String.valueOf(snowMaker.nextId()));
            questionDetailEntity = questionDetailDto.dto2Entity();
            questionDetailEntity.setCreatedDate(new Date());
            save(questionDetailEntity);
            return ResultModel.buildSuccessResultModel(null,questionDetailEntity.getQuestionId().toString());
        }
        Optional<QuestionDetailEntity> questionDetailEntityOptional = Optional.ofNullable(getById(Long.parseLong(questionDetailDto.getQuestionId())));
        if (questionDetailEntityOptional.isPresent()) {
            questionDetailEntity = questionDetailDto.dto2Entity();
            questionDetailEntity.setUpdatedDate(new Date());
            updateById(questionDetailEntity);
        }else{
            questionDetailEntity = questionDetailDto.dto2Entity();
            questionDetailEntity.setCreatedDate(new Date());
            save(questionDetailEntityOptional.orElseGet(questionDetailDto::dto2Entity));
        }
        return ResultModel.buildSuccessResultModel(null,questionDetailDto.getQuestionId());
    }

    /**
     * 获取题库的题目索引
     * */
    @Override
    public ResultModel<Map<String, List<QuestionIndexDto>>> getQuestionIndexByQuestionRepoId(String questionRepoId) {
        List<QuestionDetailEntity> questionDetailEntityList = list(
                new QueryWrapper<QuestionDetailEntity>().eq("question_repo_id", questionRepoId)
        );
        Map<String, List<QuestionIndexDto>> data = questionDetailEntityList.stream().collect(Collectors.toMap(
                questionDetailEntity -> {
                    return questionDetailEntity.getQuestionType().toString();
                },
                questionDetailEntity -> {
                    List<QuestionIndexDto> questionIndexDtoList = new LinkedList<>();
                    questionIndexDtoList.add(QuestionIndexDto.buildQuestionIndexDto(questionDetailEntity));
                    return questionIndexDtoList;
                },
                (List<QuestionIndexDto> l1, List<QuestionIndexDto> l2) -> {
                    l1.addAll(l2);
                    return l1;
                })
        );
        return ResultModel.buildSuccessResultModel(null,data);
    }

    /**
     * 通过id获取题目
     * */
    @Override
    public ResultModel<QuestionDetailDto> getQuestionById(String questionId) {
        Optional<QuestionDetailEntity> questionDetailEntityOptional = Optional.ofNullable(getById(questionId));
        if (questionDetailEntityOptional.isPresent()) {
            return ResultModel.buildSuccessResultModel(null,questionDetailEntityOptional.get().entity2Dto());
        }
        return ResultModel.buildFailResultModel("",null);
    }

    /**
     * 删除题目
     * */
    @Override
    @Transactional
    public ResultModel<String> deleteQuestionById(Long questionId,UserDto userDto) {
        if(baseMapper.deleteById(questionId)==0){
            return questionProgramService.deleteQuestionProgramById(questionId,userDto);
        }
        return ResultModel.buildSuccessResultModel();
    }

    /**
     * 获取编程题目录
     * */
    @Override
    public ResultModel<Map<QuestionType,List<QuestionIndexDto>>> getProgramQuestionIndexById(Long questionRepoId) {
        List<QuestionProgramEntity> questionProgramEntityList = questionProgramMapper.selectList(
                new QueryWrapper<QuestionProgramEntity>().eq("question_repo_id", questionRepoId)
        );
        List<QuestionIndexDto> questionIndexDtoList = questionProgramEntityList.stream().map(QuestionIndexDto::buildQuestionIndexDto).collect(Collectors.toList());
        Map<QuestionType, List<QuestionIndexDto>> res = new HashMap<>();
        res.put(QuestionType.PROGRAMMING_QUESTION,questionIndexDtoList);
        return ResultModel.buildSuccessResultModel(null,res);
    }

    /**
     * 获取题目列表
     * */
    @Override
    public ResultModel<List<QuestionIndexDto>> getQuestionList(Long questionRepoId) {
        QuestionRepoEntity questionRepoEntity = questionRepoMapper.selectById(questionRepoId);
        if(questionRepoEntity == null){
            return ResultModel.buildFailResultModel("not exist",null);
        }
        if(QuestionRepoType.PAPER.equals(questionRepoEntity.getQuestionRepoType())){
            List<QuestionDetailEntity> questionDetailEntityList = list(
                    new QueryWrapper<QuestionDetailEntity>()
                            .eq("question_repo_id", questionRepoId)
            );

            List<QuestionIndexDto> questionDetailDtoList = questionDetailEntityList.stream()
                    .map(questionDetailEntity -> new QuestionIndexDto(String.valueOf(questionDetailEntity.getQuestionId()),questionDetailEntity.getQuestionType(),null,null)).collect(Collectors.toList());
            return ResultModel.buildSuccessResultModel(null,questionDetailDtoList);
        }
        if(QuestionRepoType.PROGRAMMING.equals(questionRepoEntity.getQuestionRepoType())){
            List<QuestionProgramEntity> questionProgramEntityList = questionProgramMapper.selectList(
                    new QueryWrapper<QuestionProgramEntity>().eq("question_repo_id", questionRepoId)
            );
            List<QuestionIndexDto> questionList = questionProgramEntityList.stream()
                    .map(questionProgramEntity -> new QuestionIndexDto(String.valueOf(questionProgramEntity.getQuestionId()),QuestionType.PROGRAMMING_QUESTION,null,null)).collect(Collectors.toList());
            return ResultModel.buildSuccessResultModel(null,questionList);
        }
        return ResultModel.buildFailResultModel("Type error!",null);
    }

    /**
     * 获取试卷题目
     * */
    @Override
    public ResultModel<List<QuestionDetailDto>> getQuestionDetailListByRepoId(Long questionRepoId) {
        List<QuestionDetailEntity> questionRepoEntityList = list(
                new QueryWrapper<QuestionDetailEntity>().eq("question_repo_id", questionRepoId)
        );
        List<QuestionDetailDto> questionDetailDtoList = questionRepoEntityList.stream().map(QuestionDetailEntity::entity2Dto).collect(Collectors.toList());
        return ResultModel.buildSuccessResultModel(null,questionDetailDtoList);
    }

    /**
     * 获取编程题目
     * */
    @Override
    public ResultModel<List<QuestionProgramDto>> getQuestionProgramListByRepoId(Long questionRepoId) {
        List<QuestionProgramEntity> questionProgramEntityList = questionProgramService.list(
                new QueryWrapper<QuestionProgramEntity>().eq("question_repo_id", questionRepoId)
        );
        List<QuestionProgramDto> questionProgramDtoList = questionProgramEntityList.stream().map(QuestionProgramEntity::entity2Dto).collect(Collectors.toList());
        return ResultModel.buildSuccessResultModel(null,questionProgramDtoList);
    }

}
