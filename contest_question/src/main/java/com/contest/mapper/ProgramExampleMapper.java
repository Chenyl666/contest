package com.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.contest.entity.question.ProgramExampleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProgramExampleMapper extends BaseMapper<ProgramExampleEntity> {
    @Update("update program_example set example_number = example_number-1 where question_id = #{questionId} and example_number > #{exampleNumber}")
    public void moveDataLessThanExampleNumber(@Param("questionId")Long questionId,@Param("exampleNumber")Integer exampleNumber);
}
