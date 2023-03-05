package com.contest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.contest.dto.user.UserDto;
import com.contest.entity.question.ProgramExampleEntity;
import com.contest.result.ResultModel;

public interface ProgramExampleService extends IService<ProgramExampleEntity> {
    /**
     * 删除测试用例
     * */
    public ResultModel<String> deleteByQuestionIdAndExampleNumber(Long questionId, Integer exampleNumber, UserDto userDto);
}
