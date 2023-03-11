package com.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.contest.entity.notify.NotifyMessageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface NotifyMessageMapper extends BaseMapper<NotifyMessageEntity> {
    @Update("update notify_message set has_read = 1 where message_id = #{messageId}")
    public void setHasRead(@Param("messageId")Long messageId);
}
