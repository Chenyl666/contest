package com.contest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.contest.dto.question.QuestionIndexDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.contest.ContestDetailEntity;
import com.contest.entity.contest.ContestEnrollEntity;
import com.contest.entity.online.ContestAnswerEntity;
import com.contest.mapper.ContestDetailMapper;
import com.contest.mapper.ContestEnrollMapper;
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

    @Resource
    private ContestEnrollMapper contestEnrollMapper;

    private static final SnowMaker snowMaker = new SnowMaker(1);

    /**
     * 初始化竞赛场次
     * */
    @Override
    @Transactional
    public ResultModel<String> initContest(UserDto userDto, Long contestId) {
        long count = contestAnswerService.count(
                new QueryWrapper<ContestAnswerEntity>()
                        .eq("contest_id", contestId)
                        .eq("created_by", userDto.getUserId())
        );
        if(count != 0){
            return ResultModel.buildSuccessResultModel();
        }
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

    /**
     * 获取比赛权限
     * */
    @Override
    public ResultModel<String> checkContestStatus(UserDto userDto, Long contestId) {
        // 检查时间是否符合
        ContestDetailEntity contestDetailEntity = contestDetailMapper.selectById(contestId);
        long startTimeStamp = contestDetailEntity.getContestStartTime().getTime();
        long endTimeStamp = contestDetailEntity.getContestEndTime().getTime();
        long nowTimeStamp = System.currentTimeMillis();
        if(nowTimeStamp < startTimeStamp || nowTimeStamp > endTimeStamp){
            return ResultModel.buildFailResultModel("不在竞赛时间范围内！");
        }
        // 检查用户是否已经交过卷
        ContestEnrollEntity contestEnrollEntity = contestEnrollMapper.selectOne(
                new QueryWrapper<ContestEnrollEntity>()
                        .eq("contest_id", contestDetailEntity.getContestId())
                        .eq("user_id", userDto.getUserId())
        );
        if (ContestEnrollEntity.Status.END.equals(contestEnrollEntity.getStatus())) {
            return ResultModel.buildFailResultModel("已经提交过试卷！");
        }
        return ResultModel.buildSuccessResultModel();
    }
}
