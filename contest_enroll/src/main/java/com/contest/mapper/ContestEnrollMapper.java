package com.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.contest.entity.contest.ContestEnrollEntity;
import com.contest.entity.contest.ContestEnrollMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ContestEnrollMapper extends BaseMapper<ContestEnrollEntity> {

    @Select("select ce.user_id,ce.pay,ce.created_date,ce.enroll_id,cd.contest_subject,cd.contest_id,ct.type_name from contest_detail cd,contest_enroll ce,contest_type ct\n" +
            "where cd.contest_id = ce.contest_id and ct.type_id = cd.contest_type_id and cd.created_by = #{creator} and cd.contest_id = #{contestId}")
    public List<ContestEnrollMessage> getContestEnrollMessageByCreatedBy(@Param("creator") String createdBy,@Param("contestId") Long contestId);

}
