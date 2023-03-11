package com.contest.service;

import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;

public interface ContestInitService {
    public ResultModel<String> initContest(UserDto userDto,Long contestId);
}
