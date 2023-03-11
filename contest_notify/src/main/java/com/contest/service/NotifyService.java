package com.contest.service;

import com.contest.dto.notify.NotifyDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.notify.NotifyMessageEntity;
import com.contest.result.ResultModel;

import java.util.List;

public interface NotifyService {

    /**
     * 保存通知信息
     * */
    public void saveNotifyMessage(NotifyMessageEntity notifyMessageEntity);

    /**
     * 获取通知列表
     * */
    public ResultModel<List<NotifyDto>> getNotifyList(UserDto userDto, int page);

    /**
     * 标记消息为已读
     * */
    public void setHasRead(Long messageId);

    /**
     * 获取未读消息数量
     * */
    public ResultModel<Integer> getUnreadNotifyMessageCount(UserDto userDto);

    /**
     * 通过id删除消息
     * */
    public ResultModel<String> deleteById(Long messageId);

}
