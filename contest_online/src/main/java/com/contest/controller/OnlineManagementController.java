package com.contest.controller;

import com.contest.dto.online.OnlineStatusDto;
import com.contest.result.ResultModel;
import com.contest.service.OnlineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/online")
public class OnlineManagementController {

    @Resource
    private OnlineService onlineService;

    /**
     * 获取用户实时状态
     * */
    @GetMapping("/status/list/{contest_id}")
    public ResultModel<List<OnlineStatusDto>> getOnlineStatusList(@PathVariable("contest_id") Long contestId){
        return onlineService.getOnlineStatusList(contestId);
    }

}
