package com.contest.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.contest.async.processor.channel.AsyncChannel;
import com.contest.dto.user.UserDto;
import com.contest.entity.question.ProgramExampleEntity;
import com.contest.mapper.ProgramExampleMapper;
import com.contest.result.ResultModel;
import com.contest.service.ProgramExampleService;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProgramExampleServiceImpl extends ServiceImpl<ProgramExampleMapper, ProgramExampleEntity> implements ProgramExampleService {

    @Resource
    private AsyncChannel asyncChannel;

    @Override
    @Transactional
    public ResultModel<String> deleteByQuestionIdAndExampleNumber(Long questionId, Integer exampleNumber, UserDto userDto){
        List<ProgramExampleEntity> programExampleEntityList = list(
                new QueryWrapper<ProgramExampleEntity>()
                        .eq("question_id", questionId)
                        .eq("example_number", exampleNumber)
        );
        programExampleEntityList.forEach(programExampleEntity -> {
            String url = programExampleEntity.getExampleUrl();
            Map<String,String> payload = new HashMap<>();
            payload.put("userDto", JSON.toJSONString(userDto));
            payload.put("url",url);
            asyncChannel.deleteFileByDownloadUrl().send(MessageBuilder.withPayload(payload).build());
        });
        remove(
                new QueryWrapper<ProgramExampleEntity>()
                        .eq("question_id",questionId)
                        .eq("example_number",exampleNumber)
        );
        baseMapper.moveDataLessThanExampleNumber(questionId, exampleNumber);
        return ResultModel.buildSuccessResultModel();
    }
}
