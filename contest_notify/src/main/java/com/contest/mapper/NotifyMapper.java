package com.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.contest.entity.notify.NotifyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NotifyMapper extends BaseMapper<NotifyEntity> {
    @Select("select * from notify where receiver = #{receiver} order by has_read,created_date desc limit #{offset},#{page_size}")
    public List<NotifyEntity> getNotifyEntityList(@Param("receiver") String receiver,@Param("offset")int offset,@Param("page_size")int pageSize);

    @Select("select count(message_id) from notify where receiver = #{receiver} and has_read = 0")
    public Integer getUnreadCount(@Param("receiver") String receiver);
}
