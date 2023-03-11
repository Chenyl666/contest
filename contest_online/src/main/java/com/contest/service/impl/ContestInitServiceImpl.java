package com.contest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.contest.dto.question.QuestionIndexDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.contest.ContestDetailEntity;
import com.contest.entity.online.ContestAnswerEntity;
import com.contest.mapper.ContestDetailMapper;
import com.contest.result.ResultFlag;
import com.contest.result.ResultModel;
import com.contest.service.ContestAnswerService;
import com.contest.service.ContestInitService;
import com.contest.service.QuestionService;
import com.contest.util.SnowMaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class ContestInitServiceImpl implements ContestInitService {

    @Resource
    private ContestDetailMapper contestDetailMapper;

    @Resource
    private QuestionService questionService;

    @Resource
    private ContestAnswerService contestAnswerService;

    private static final SnowMaker snowMaker = new SnowMaker(1);

    /**
     * 初始化竞赛场次
     * */
    @Override
    @Transactional
    public ResultModel<String> initContest(UserDto userDto, Long contestId) {
        ContestDetailEntity contestDetailEntity = contestDetailMapper.selectOne(
                new QueryWrapper<ContestDetailEntity>().eq("contest_id", contestId));
        Long questionRepoId = contestDetailEntity.getQuestionRepoId();
        ResultModel<List<QuestionIndexDto>> remoteInvokeResult =
                questionService.getQuestionsByRepoId(questionRepoId);
        if(!ResultFlag.SUCCESS.equals(remoteInvokeResult.getResultFlag())){
            return ResultModel.buildFailResultModel();
        }
        List<QuestionIndexDto> answerList = remoteInvokeResult.getData();
        List<ContestAnswerEntity> contestAnswerEntityList = new LinkedList<>();
        answerList.forEach(QuestionIndexDto -> {
            ContestAnswerEntity contestAnswerEntity = ContestAnswerEntity
                    .builder()
                    .answerId(snowMaker.nextId())
                    .answerContent(null)
                    .contestId(contestId)
                    .createdBy(userDto.getUserId())
                    .questionId(Long.parseLong(QuestionIndexDto.getQuestionId()))
                    .createdDate(new Date())
                    .questionType(QuestionIndexDto.getQuestionType())
                    .score(0f)
                    .judgeComment(null)
                    .updatedDate(new Date())
                    .build();
            contestAnswerEntityList.add(contestAnswerEntity);
        });
        contestAnswerService.saveBatch(contestAnswerEntityList);
        return ResultModel.buildSuccessResultModel();
    }
}
