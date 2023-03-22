package com.contest.service;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.contest.ContestDetailDto;
import com.contest.dto.contest.ContestEnrollDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.contest.ContestEnrollMessage;
import com.contest.entity.contest.ContestType;
import com.contest.result.ResultModel;
import org.apache.ibatis.annotations.ResultMap;

import java.util.List;

public interface EnrollService {
    public ResultModel<Long> saveContestDetail(ContestDetailDto contestDetailDto, UserDto userDto);

    public ResultModel<List<ContestDetailDto>> getContestDetailByUser(UserDto userDto);

    public ResultModel<UserDto> getCreatorByContestId(String contestId);

    public ResultModel<ContestDetailDto> getContestDetailById(String contestId);

    public ResultModel<String> updateContestDetail(ContestDetailDto contestDetailDto);

    public ResultModel<ContestType> getContestTypeById(Long contestId);

    public ResultModel<String> importQuestionRepo(Long contestId,Long questionRepoId);

    public ResultModel<ContestEnrollDto> getContestEnrollDto(Long contestId,UserDto userDto);

    public ResultModel<List<ContestEnrollMessage>> getContestEnrollMessageListByCreatedBy(UserDto userDto,Long contestId);

    public ResultModel<String> deleteEnrollById(Long enrollId,UserDto userDto);

    public ResultModel<List<ContestEnrollDto>> getContestEnrollListByContestId(Long contestId);
}

