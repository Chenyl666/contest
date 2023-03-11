package com.contest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.contest.dto.contest.ContestDetailDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.contest.ContestDetailEntity;
import com.contest.result.ResultModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface ContestDetailService extends IService<ContestDetailEntity> {
    public ResultModel<List<ContestDetailDto>> getOrganizerContestDetailList(UserDto userDto);
}
