package com.contest.service;

import com.contest.dto.contest.ContestResultDto;
import com.contest.dto.contest.ContestUserResult;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ContestResultService {
    /**
     * 生成成绩
     * */
    public ResultModel<String> generateContestResult(Long contestId);

    /**
     * 发布成绩
     * */
    public ResultModel<String> publishContestResult(Long contestId);

    /**
     * 获取成绩列表
     * */
    public ResultModel<List<ContestResultDto>> getContestResultListByContestId(Long contestId);

    /**
     * 导出成绩名单
     * */
    public void exportContestResultAsExcel(Long contestId, HttpServletResponse response);

    /**
     * 获取用户的竞赛成绩
     * */
    public ResultModel<ContestUserResult> getContestUserResult(Long contestId, UserDto userDto);
}
