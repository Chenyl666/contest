package com.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.contest.entity.contest.ContestDetailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ContestDetailMapper extends BaseMapper<ContestDetailEntity> {
    @Select("select * from contest_detail where contest_id in (select contest_id from contest_enroll where user_id = #{user_id})")
    public List<ContestDetailEntity> getContestDetailEntityListByOrganizerUserId(@Param("user_id") String userId);
}
