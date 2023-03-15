package com.contest.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.contest.dto.contest.ContestAnswerResultDto;
import com.contest.dto.online.ContestAnswerDto;
import com.contest.dto.online.ContestProgramDto;
import com.contest.dto.question.QuestionDetailDto;
import com.contest.dto.question.QuestionProgramDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.contest.ContestDetailEntity;
import com.contest.entity.contest.ContestEnrollEntity;
import com.contest.entity.online.ContestAnswerEntity;
import com.contest.enu.QuestionType;
import com.contest.mapper.ContestAnswerMapper;
import com.contest.mapper.ContestDetailMapper;
import com.contest.mapper.ContestEnrollMapper;
import com.contest.result.ResultFlag;
import com.contest.result.ResultModel;
import com.contest.service.ContestAnswerService;
import com.contest.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContestAnswerServiceImpl extends ServiceImpl<ContestAnswerMapper, ContestAnswerEntity> implements ContestAnswerService {

    @Resource
    private ContestDetailMapper contestDetailMapper;

    @Resource
    private QuestionService questionService;

    @Resource
    private ContestEnrollMapper contestEnrollMapper;

    /**
     * 获取题目和回答信息
     */
    @Override
    @Transactional
    public ResultModel<Map<QuestionType, List<ContestAnswerDto>>> getContestAnswers(UserDto userDto, Long contestId) {
        ContestDetailEntity contestDetailEntity = contestDetailMapper.selectById(contestId);
        if (contestDetailEntity == null) {
            return ResultModel.buildFailResultModel("竞赛不存在！", null);
        }
        ResultModel<List<QuestionDetailDto>> removeResult = questionService.getQuestionListByRepoId(contestDetailEntity.getQuestionRepoId());
        if (!ResultFlag.SUCCESS.equals(removeResult.getResultFlag())) {
            return ResultModel.buildFailResultModel("系统繁忙！", null);
        }
        List<QuestionDetailDto> questionDetailDtoList = removeResult.getData();
        Map<String, QuestionDetailDto> questionDetailDtoMap = questionDetailDtoList.stream().collect(
                Collectors.toMap(QuestionDetailDto::getQuestionId, q -> {
                    if (QuestionType.SUPPLEMENT_QUESTION.equals(q.getQuestionType())) {
                        List<Object> jsonList = JSON.parseObject(q.getQuestionReferenceAnswer(), List.class);
                        q.setQuestionReferenceAnswer(String.valueOf(jsonList.size()));
                    } else {
                        q.setQuestionReferenceAnswer(null);
                    }
                    return q;
                })
        );
        List<ContestAnswerEntity> contestAnswerEntityList = list(
                new QueryWrapper<ContestAnswerEntity>().eq("contest_id", contestId).eq("created_by", userDto.getUserId())
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
        return ResultModel.buildSuccessResultModel("", contestAnswerDtoMap);
    }

    /**
     * 获取编程题目的回答情况
     */
    @Override
    public ResultModel<List<ContestProgramDto>> getContestPrograms(UserDto userDto, Long contestId) {
        ContestDetailEntity contestDetailEntity = contestDetailMapper.selectById(contestId);
        if (contestDetailEntity == null) {
            return ResultModel.buildFailResultModel("系统繁忙！", null);
        }
        ResultModel<List<QuestionProgramDto>> remoteInvokeResult = questionService.getQuestionProgramListByRepoId(contestDetailEntity.getQuestionRepoId());
        if (!ResultFlag.SUCCESS.equals(remoteInvokeResult.getResultFlag())) {
            return ResultModel.buildFailResultModel("系统繁忙！", null);
        }
        List<QuestionProgramDto> questionProgramDtoList = remoteInvokeResult.getData();
        Map<String, QuestionProgramDto> questionProgramDtoMap = questionProgramDtoList.stream().collect(Collectors.toMap(
                QuestionProgramDto::getQuestionId,
                questionProgramDto -> questionProgramDto
        ));
        List<ContestAnswerEntity> contestAnswerEntityList = list(
                new QueryWrapper<ContestAnswerEntity>()
                        .eq("contest_id", contestId)
                        .eq("created_by", userDto.getUserId())
        );
        List<ContestProgramDto> contestProgramDtoList = contestAnswerEntityList.stream().map(
                contestAnswerEntity -> contestAnswerEntity.entity2Dto(questionProgramDtoMap.get(String.valueOf(contestAnswerEntity.getQuestionId())))
        ).collect(Collectors.toList());
        return ResultModel.buildSuccessResultModel(null, contestProgramDtoList);
    }

//    /**
//     * 保存常规题目
//     * */
//    @Override
//    public ResultModel<String> savePaperQuestionAnswer(ContestAnswerDto contestAnswerDto) {
//        ContestAnswerEntity contestAnswerEntity = contestAnswerDto.dto2Entity();
//        updateById(contestAnswerEntity);
//        return ResultModel.buildSuccessResultModel();
//    }

    /**
     * 保存常规题目回答记录
     */
    @Override
    public ResultModel<String> save(Map<QuestionType, List<ContestAnswerDto>> contestAnswerDtoMap) {
        List<ContestAnswerDto> contestAnswerDtoList = new LinkedList<>();
        for (QuestionType questionType : contestAnswerDtoMap.keySet()) {
            List<ContestAnswerDto> contestAnswerDtoSet = contestAnswerDtoMap.get(questionType);
            contestAnswerDtoList.addAll(contestAnswerDtoSet);
        }
        List<ContestAnswerEntity> contestAnswerEntityList = contestAnswerDtoList.stream().map(
                ContestAnswerDto::dto2Entity).collect(Collectors.toList()
        );
        updateBatchById(contestAnswerEntityList);
        return ResultModel.buildSuccessResultModel();
    }

    /**
     * 更新用户的比赛状态
     */
    @Override
    public ResultModel<String> updateUserContestStatus(
            UserDto userDto, Long contestId, Integer status
    ) {
        ContestEnrollEntity contestEnrollEntity = new ContestEnrollEntity();
        contestEnrollEntity.setUserId(userDto.getUserId());
        contestEnrollEntity.setContestId(contestId);
        switch (status) {
            case 0: {
                contestEnrollEntity.setNoStartStatus();
                break;
            }
            case 1: {
                contestEnrollEntity.setIngStatus();
                break;
            }
            case 2: {
                contestEnrollEntity.setEndStatus();
                break;
            }
        }
        contestEnrollMapper.update(
                contestEnrollEntity,
                new QueryWrapper<ContestEnrollEntity>()
                        .eq("user_id", userDto.getUserId())
                        .eq("contest_id", contestId)
        );
        return ResultModel.buildSuccessResultModel();
    }

    /**
     * 获取常规题目的回答情况
     */
    @Override
    public ResultModel<List<ContestAnswerResultDto>> getSupplementAndAnswerQuestionList(Long contestId) {
        ContestDetailEntity contestDetaiLEntity = contestDetailMapper.selectById(contestId);
        ResultModel<List<QuestionDetailDto>> remoteInvokeRes = questionService.getQuestionDetailDtoListByRepoId(contestDetaiLEntity.getQuestionRepoId());
        if (!ResultFlag.SUCCESS.equals(remoteInvokeRes.getResultFlag())) {
            return ResultModel.buildFailResultModel("调用失败", null);
        }
        List<QuestionDetailDto> questionDetailDtoList = remoteInvokeRes.getData();
        Map<String, QuestionDetailDto> questionDetailDtoMap = questionDetailDtoList.stream().collect(Collectors.toMap(
                QuestionDetailDto::getQuestionId,
                questionDetailDto -> questionDetailDto
        ));
        List<ContestAnswerEntity> contestAnswerEntityList = list(new QueryWrapper<ContestAnswerEntity>().eq("contest_id", contestId));
        List<ContestAnswerResultDto> contestAnswerResultDtoList = contestAnswerEntityList.stream().map(
                contestAnswerEntity -> contestAnswerEntity.entity2ResultDto(
                        questionDetailDtoMap.get(contestAnswerEntity.getQuestionId().toString())
                )
        ).collect(Collectors.toList());
        return ResultModel.buildSuccessResultModel(null, contestAnswerResultDtoList);
    }

    /**
     * 手动判题
     */
    @Override
    public ResultModel<String> judgeByEnrollId(Long answerId, Float score) {
        updateById(
                ContestAnswerEntity
                        .builder()
                        .answerId(answerId)
                        .score(score)
                        .hasJudge(true)
                        .build()
        );
        return ResultModel.buildSuccessResultModel();
    }

    /**
     * 开启自动判题
     */
    @Override
    @Transactional
    public ResultModel<String> judgeAutoByContestId(Long contestId) {
        ContestDetailEntity contestDetailEntity = contestDetailMapper.selectById(contestId);
        Long questionRepoId = contestDetailEntity.getQuestionRepoId();
        ResultModel<List<QuestionDetailDto>> remoteInvokeRes = questionService.getQuestionListByRepoId(questionRepoId);
        if (!ResultFlag.SUCCESS.equals(remoteInvokeRes.getResultFlag())) {
            return ResultModel.buildFailResultModel();
        }
        List<QuestionDetailDto> questionDetailDtoList = remoteInvokeRes.getData();
        Map<String, QuestionDetailDto> questionDetailDtoMap = questionDetailDtoList.stream().collect(
                Collectors.toMap(
                        QuestionDetailDto::getQuestionId,
                        questionDetailDto -> questionDetailDto
                )
        );
        List<ContestAnswerEntity> contestAnswerEntityList = list(new QueryWrapper<ContestAnswerEntity>().eq("contest_id", contestId));
        contestAnswerEntityList = contestAnswerEntityList.stream().filter(
                contestAnswerEntity -> QuestionType.SINGLE_OPTION_QUESTION.equals(contestAnswerEntity.getQuestionType())
                        || QuestionType.JUDGE_QUESTION.equals(contestAnswerEntity.getQuestionType())
        ).collect(Collectors.toList());
        contestAnswerEntityList.forEach(contestAnswerEntity -> {
            String answerContent = contestAnswerEntity.getAnswerContent();
            QuestionDetailDto questionDetailDto = questionDetailDtoMap.get(contestAnswerEntity.getQuestionId().toString());
            if (questionDetailDto == null) {
                throw new RuntimeException("data not exist!");
            }
            String referenceAnswer = questionDetailDto.getQuestionReferenceAnswer();
            boolean pass;
            if(answerContent == null || referenceAnswer == null){
                pass = false;
            }else{
                pass = answerContent.equals(referenceAnswer);
            }
            contestAnswerEntity.setHasJudge(true);
            contestAnswerEntity.setScore(pass ? questionDetailDto.getScore() : 0);
            updateById(contestAnswerEntity);
        });
        return ResultModel.buildSuccessResultModel();
    }
}
