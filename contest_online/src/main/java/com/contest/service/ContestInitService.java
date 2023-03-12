package com.contest.service;

import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;

public interface ContestInitService {
    /**
     * 初始化比赛
     * */
    public ResultModel<String> initContest(UserDto userDto,Long contestId);

    /**
     * 获取比赛权限
     * */
    public ResultModel<String> checkContestStatus(UserDto userDto,Long contestId);
}
