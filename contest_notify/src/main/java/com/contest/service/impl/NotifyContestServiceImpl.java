package com.contest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.contest.async.provider.NotifyMessageProvider;
import com.contest.dto.contest.ContestEnrollDto;
import com.contest.dto.notify.NotifyContestDto;
import com.contest.dto.notify.NotifyContestSubmitDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.contest.ContestDetailMessageEntity;
import com.contest.entity.notify.NotifyContestEntity;
import com.contest.entity.notify.NotifyMessageEntity;
import com.contest.entity.user.UserEntity;
import com.contest.service.EnrollService;
import com.contest.mapper.NotifyContestMapper;
import com.contest.mapper.UserMapper;
import com.contest.result.ResultFlag;
import com.contest.result.ResultModel;
import com.contest.service.NotifyContestService;
import com.contest.smtp.SmtpMailSender;
import com.contest.util.SnowMaker;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotifyContestServiceImpl extends ServiceImpl<NotifyContestMapper, NotifyContestEntity> implements NotifyContestService {

    private static final SnowMaker snowMaker = new SnowMaker(1);

    @Resource
    private EnrollService enrollService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private NotifyMessageProvider notifyMessageProvider;

    /**
     * 发布竞赛通知
     * */
    @Override
    public ResultModel<String> publishNotifyContest(NotifyContestSubmitDto notifyContestSubmitDto, UserDto userDto){
        NotifyContestEntity notifyContestEntity = notifyContestSubmitDto.dto2Entity(snowMaker.nextId(), userDto.getUserId());
        save(notifyContestEntity);
        ResultModel<List<ContestEnrollDto>> remoteInvokeResult = enrollService.getEnrollListByContestId(notifyContestEntity.getContestId());
        if(!ResultFlag.SUCCESS.equals(remoteInvokeResult.getResultFlag())){
            return ResultModel.buildFailResultModel();
        }
        List<ContestEnrollDto> contestEnrollDtoList = remoteInvokeResult.getData();
        List<String> userIdList = contestEnrollDtoList.stream().map(ContestEnrollDto::getUserId).collect(Collectors.toList());
        List<UserEntity> userEntityList = userMapper.selectList(
                new QueryWrapper<UserEntity>().in("user_id", userIdList)
        );
        userEntityList.forEach(userEntity -> {
            notifyMessageProvider.asyncSendNotifyMessage(
                    buildNotifyMessageEntity(notifyContestEntity, userEntity.getUserId())
            );
        });
        return ResultModel.buildSuccessResultModel();
    }

    /**
     * 获取竞赛通知
     * */
    @Override
    public ResultModel<List<NotifyContestDto>> getNotifyContestListByContestId(Long contestId, UserDto userDto) {
        ResultModel<ContestDetailMessageEntity> remoteInvokeResult = enrollService.getContestDetailMessageById(contestId.toString());
        if(!ResultFlag.SUCCESS.equals(remoteInvokeResult.getResultFlag())){
            return ResultModel.buildFailResultModel(null,null);
        }
        ContestDetailMessageEntity contestDetailMessageEntity = remoteInvokeResult.getData();
        List<NotifyContestEntity> notifyContestEntityList = list(
                new QueryWrapper<NotifyContestEntity>().eq("contest_id", contestId).orderByDesc("created_date")
        );
        List<NotifyContestDto> notifyContestDtoList = notifyContestEntityList.stream().map(notifyContestEntity ->
                NotifyContestDto
                        .builder()
                        .contestNotifyId(notifyContestEntity.getContestNotifyId().toString())
                        .contestSubject(contestDetailMessageEntity.getContestSubject())
                        .createdDate(notifyContestEntity.getCreatedDate())
                        .createdBy(userDto.getUserName())
                        .title(notifyContestEntity.getTitle())
                        .content(notifyContestEntity.getContent())
                        .build()
        ).collect(Collectors.toList());
        return ResultModel.buildSuccessResultModel(null,notifyContestDtoList);
    }

    /**
     * 删除竞赛通知
     * */
    @Override
    public ResultModel<String> deletePublishByContestNotifyId(Long contestNotifyId) {
        removeById(contestNotifyId);
        return ResultModel.buildSuccessResultModel();
    }

    private NotifyMessageEntity buildNotifyMessageEntity(NotifyContestEntity notifyContestEntity,String receiver){
        return NotifyMessageEntity
                .builder()
                .messageId(snowMaker.nextId())
                .messageTitle(notifyContestEntity.getTitle())
                .messageContent(notifyContestEntity.getContent())
                .createdBy(notifyContestEntity.getCreatedBy())
                .createdDate(notifyContestEntity.getCreatedDate())
                .hasRead(false)
                .receiver(receiver)
                .build();
    }
}
