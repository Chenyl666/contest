package com.contest.service;

import com.contest.dto.online.OnlineStatusDto;
import com.contest.result.ResultModel;

import java.util.List;

public interface OnlineService {
    public ResultModel<List<OnlineStatusDto>> getOnlineStatusList(Long contestId);
}
