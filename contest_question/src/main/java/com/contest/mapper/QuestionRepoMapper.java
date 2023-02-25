package com.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.contest.entity.question.QuestionRepoEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionRepoMapper extends BaseMapper<QuestionRepoEntity> {
}
