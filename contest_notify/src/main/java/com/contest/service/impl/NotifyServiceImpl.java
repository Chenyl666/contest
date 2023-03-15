package com.contest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.contest.dto.notify.NotifyDto;
import com.contest.dto.notify.NotifyEmailDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.notify.NotifyEntity;
import com.contest.entity.notify.NotifyMessageEntity;
import com.contest.entity.user.UserEntity;
import com.contest.mapper.NotifyMapper;
import com.contest.mapper.NotifyMessageMapper;
import com.contest.mapper.UserMapper;
import com.contest.result.ResultModel;
import com.contest.service.NotifyService;
import com.contest.smtp.SmtpMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotifyServiceImpl implements NotifyService {

    @Resource
    private NotifyMapper notifyMapper;

    @Resource
    private SmtpMailSender smtpMailSender;

    @Resource
    private NotifyMessageMapper notifyMessageMapper;

    @Resource
    private UserMapper userMapper;

    private static final int pageSize = 4;

    /**
     * 保存通知消息
     * */
    @Override
    public void saveNotifyMessage(NotifyMessageEntity notifyMessageEntity) {
        NotifyEmailDto notifyEmailDto = NotifyEmailDto
                .builder()
                .messageContent(notifyMessageEntity.getMessageContent())
                .messageTitle(notifyMessageEntity.getMessageTitle())
                .receiver(notifyMessageEntity.getReceiver())
                .createdBy("system")
                .build();
        notifyMessageMapper.insert(notifyMessageEntity);
        sendEmail(notifyEmailDto);
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

    private void sendEmail(NotifyEmailDto notifyEmailDto){
        String receiverId = notifyEmailDto.getReceiver();
        UserEntity user = userMapper.selectById(receiverId);
        smtpMailSender.sendSimpleEmail(notifyEmailDto.getMessageTitle(), notifyEmailDto.getMessageContent(), user.getUserEmail());
    }

}
