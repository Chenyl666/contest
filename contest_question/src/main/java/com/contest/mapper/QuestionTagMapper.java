package com.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.contest.entity.question.QuestionTagEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionTagMapper extends BaseMapper<QuestionTagEntity> {
}
