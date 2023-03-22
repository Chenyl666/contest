package com.contest.service;

import com.contest.dto.contest.ContestEnrollDto;
import com.contest.entity.contest.ContestDetailMessageEntity;
import com.contest.result.ResultModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("contest-enroll")
public interface EnrollService {
    @GetMapping("/contest/enroll/list/{contest_id}")
    public ResultModel<List<ContestEnrollDto>> getEnrollListByContestId(@PathVariable("contest_id")Long contestId);

    @GetMapping("/contest/enroll/message/get/{contest_id}")
    public ResultModel<ContestDetailMessageEntity> getContestDetailMessageById(@PathVariable("contest_id") String contestId);
}
