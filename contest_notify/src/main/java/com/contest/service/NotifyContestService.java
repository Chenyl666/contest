package com.contest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.contest.dto.notify.NotifyContestDto;
import com.contest.dto.notify.NotifyContestSubmitDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.notify.NotifyContestEntity;
import com.contest.result.ResultModel;

import java.util.List;

public interface NotifyContestService extends IService<NotifyContestEntity> {
    /**
     * 发布竞赛通知
     * */
    public ResultModel<String> publishNotifyContest(NotifyContestSubmitDto notifyContestSubmitDto, UserDto userDto);

    /**
     * 获取竞赛通知
     * */
    public ResultModel<List<NotifyContestDto>> getNotifyContestListByContestId(Long contestId, UserDto userDto);

    /**
     * 删除竞赛通知
     * */
    public ResultModel<String> deletePublishByContestNotifyId(Long contestNotifyId);
}
