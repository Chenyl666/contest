package com.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.contest.entity.question.QuestionDetailEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionDetailMapper extends BaseMapper<QuestionDetailEntity> {
}
