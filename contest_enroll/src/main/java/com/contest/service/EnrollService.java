package com.contest.service;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.contest.ContestDetailDto;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;

import java.util.List;

public interface EnrollService {
    public ResultModel<Long> saveContestDetail(ContestDetailDto contestDetailDto, UserDto userDto);

    public ResultModel<List<ContestDetailDto>> getContestDetailByUser(UserDto userDto);

    public ResultModel<UserDto> getCreatorByContestId(String contestId);

    public ResultModel<ContestDetailDto> getContestDetailById(String contestId);

    public ResultModel<String> updateContestDetail(ContestDetailDto contestDetailDto);

}

