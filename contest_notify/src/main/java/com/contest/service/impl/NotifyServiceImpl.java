package com.contest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.contest.dto.notify.NotifyDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.notify.NotifyEntity;
import com.contest.entity.notify.NotifyMessageEntity;
import com.contest.mapper.NotifyMapper;
import com.contest.mapper.NotifyMessageMapper;
import com.contest.result.ResultModel;
import com.contest.service.NotifyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotifyServiceImpl implements NotifyService {

    @Resource
    private NotifyMapper notifyMapper;

    @Resource
    private NotifyMessageMapper notifyMessageMapper;

    private static final int pageSize = 4;

    /**
     * 保存通知消息
     * */
    @Override
    public void saveNotifyMessage(NotifyMessageEntity notifyMessageEntity) {
        notifyMessageMapper.insert(notifyMessageEntity);
    }

    /**
     * 获取通知消息列表
     * */
    @Override
    public ResultModel<List<NotifyDto>> getNotifyList(UserDto userDto, int page) {
        List<NotifyEntity> notifyEntityList = notifyMapper.getNotifyEntityList(userDto.getUserId(), computePageOffset(page), pageSize);
        List<NotifyDto> notifyDtoList = notifyEntityList.stream().map(NotifyEntity::entity2Dto).collect(Collectors.toList());
        return ResultModel.buildSuccessResultModel(null,notifyDtoList);
    }

    /**
     * 标记消息为已读
     * */
    @Override
    public void setHasRead(Long messageId) {
        notifyMessageMapper.setHasRead(messageId);
    }

    /**
     * 获取未读消息的数量
     * */
    @Override
    public ResultModel<Integer> getUnreadNotifyMessageCount(UserDto userDto) {
        Integer count = notifyMapper.getUnreadCount(userDto.getUserId());
        return ResultModel.buildSuccessResultModel(null,count);
    }

    /**
     * 通过id删除消息
     * */
    @Override
    public ResultModel<String> deleteById(Long messageId) {
        notifyMessageMapper.delete(
                new QueryWrapper<NotifyMessageEntity>().eq("message_id",messageId)
        );
        return ResultModel.buildSuccessResultModel();
    }

    private int computePageOffset(int page){
        return (page-1)*pageSize;
    }

}
