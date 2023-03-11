package com.contest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.contest.dto.online.ContestAnswerDto;
import com.contest.dto.online.ContestProgramDto;
import com.contest.dto.question.QuestionDetailDto;
import com.contest.dto.question.QuestionProgramDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.contest.ContestDetailEntity;
import com.contest.entity.online.ContestAnswerEntity;
import com.contest.enu.QuestionType;
import com.contest.mapper.ContestAnswerMapper;
import com.contest.mapper.ContestDetailMapper;
import com.contest.result.ResultFlag;
import com.contest.result.ResultModel;
import com.contest.service.ContestAnswerService;
import com.contest.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContestAnswerServiceImpl extends ServiceImpl<ContestAnswerMapper, ContestAnswerEntity> implements ContestAnswerService {

    @Resource
    private ContestDetailMapper contestDetailMapper;

    @Resource
    private QuestionService questionService;

    /**
     * 获取题目和回答信息
     * */
    @Override
    @Transactional
    public ResultModel<Map<QuestionType, List<ContestAnswerDto>>> getContestAnswers(UserDto userDto, Long contestId) {
        ContestDetailEntity contestDetailEntity = contestDetailMapper.selectById(contestId);
        if(contestDetailEntity == null){
            return ResultModel.buildFailResultModel("竞赛不存在！",null);
        }
        ResultModel<List<QuestionDetailDto>> removeResult = questionService.getQuestionListByRepoId(contestDetailEntity.getQuestionRepoId());
        if(!ResultFlag.SUCCESS.equals(removeResult.getResultFlag())){
            return ResultModel.buildFailResultModel("系统繁忙！",null);
        }
        List<QuestionDetailDto> questionDetailDtoList = removeResult.getData();
        Map<String, QuestionDetailDto> questionDetailDtoMap = questionDetailDtoList.stream().collect(
                Collectors.toMap(QuestionDetailDto::getQuestionId, q -> q)
        );
        List<ContestAnswerEntity> contestAnswerEntityList = list(
                new QueryWrapper<ContestAnswerEntity>().eq("contest_id", contestId).eq("created_by",userDto.getUserId())
        );
        Map<QuestionType, List<ContestAnswerDto>> contestAnswerDtoMap = contestAnswerEntityList.stream().collect(Collectors.toMap(
                ContestAnswerEntity::getQuestionType,
                contestAnswerEntity -> {
                    ContestAnswerDto contestAnswerDto = contestAnswerEntity.entity2Dto(questionDetailDtoMap.get(String.valueOf(contestAnswerEntity.getQuestionId())));
                    List<ContestAnswerDto> list = new ArrayList<>();
                    list.add(contestAnswerDto);
                    return list;
                },
                (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                }
        ));
        return ResultModel.buildSuccessResultModel("",contestAnswerDtoMap);
    }

    /**
     * 获取编程题目的回答情况
     * */
    @Override
    public ResultModel<List<ContestProgramDto>> getContestPrograms(UserDto userDto, Long contestId) {
        ContestDetailEntity contestDetailEntity = contestDetailMapper.selectById(contestId);
        if(contestDetailEntity == null){
            return ResultModel.buildFailResultModel("系统繁忙！",null);
        }
        ResultModel<List<QuestionProgramDto>> remoteInvokeResult = questionService.getQuestionProgramListByRepoId(contestDetailEntity.getQuestionRepoId());
        if(!ResultFlag.SUCCESS.equals(remoteInvokeResult.getResultFlag())){
            return ResultModel.buildFailResultModel("系统繁忙！",null);
        }
        List<QuestionProgramDto> questionProgramDtoList = remoteInvokeResult.getData();
        Map<String, QuestionProgramDto> questionProgramDtoMap = questionProgramDtoList.stream().collect(Collectors.toMap(
                QuestionProgramDto::getQuestionId,
                questionProgramDto -> questionProgramDto
        ));
        System.out.println(questionProgramDtoMap);
        List<ContestAnswerEntity> contestAnswerEntityList = list(
                new QueryWrapper<ContestAnswerEntity>()
                        .eq("contest_id", contestId)
                        .eq("created_by", userDto.getUserId())
        );
        System.out.println(contestAnswerEntityList);
        List<ContestProgramDto> contestProgramDtoList = contestAnswerEntityList.stream().map(
                contestAnswerEntity -> contestAnswerEntity.entity2Dto(questionProgramDtoMap.get(String.valueOf(contestAnswerEntity.getQuestionId())))
        ).collect(Collectors.toList());
        return ResultModel.buildSuccessResultModel(null,contestProgramDtoList);
    }
}
