package com.contest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.contest.dto.contest.ContestDetailDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.contest.ContestDetailEntity;
import com.contest.result.ResultModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface ContestDetailService extends IService<ContestDetailEntity> {
    /**
     * 获取举办方所举办的全部比赛
     * */
    public ResultModel<List<ContestDetailDto>> getOrganizerContestDetailList(UserDto userDto);

    /**
     * 获取热点比赛数据
     * */
    public ResultModel<List<ContestDetailDto>> getHotContestDetailList();

    /**
     * 更新比赛状态
     * */
    public ResultModel<String> updateContestStatus(Long contestId);
}
