package com.contest.mapper;

import com.contest.entity.contest.ContestResultEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ContestResultMapper {
    @Select("select (select @rowNum:=@rowNum+1) as `rank`, ca.created_by as user_id,cd.contest_id,cd.contest_subject,sum(score) as sum_score\n" +
            "from contest_answer ca,contest_detail cd\n" +
            "where ca.contest_id = cd.contest_id and cd.contest_id = #{contestId}\n" +
            "group by user_id,contest_id\n" +
            "order by sum_score desc,(select @rowNum:=0);")
    public List<ContestResultEntity> getContestResultEntity(@Param("contestId")Long contestId);
}
