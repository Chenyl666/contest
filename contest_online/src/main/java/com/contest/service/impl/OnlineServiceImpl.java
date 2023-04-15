package com.contest.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.contest.dto.online.OnlineStatusDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.contest.ContestEnrollEntity;
import com.contest.mapper.ContestEnrollMapper;
import com.contest.result.ResultFlag;
import com.contest.result.ResultModel;
import com.contest.service.OnlineService;
import com.contest.service.UserService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OnlineServiceImpl implements OnlineService {

    @Resource
    private ContestEnrollMapper contestEnrollMapper;

    @Resource
    private UserService userService;

    /**
     * 获取竞赛状态
     * */
    @SneakyThrows
    @Override
    public ResultModel<List<OnlineStatusDto>> getOnlineStatusList(Long contestId) {
        List<ContestEnrollEntity> contestEnrollEntityList = contestEnrollMapper.selectList(
                new QueryWrapper<ContestEnrollEntity>().eq("contest_id", contestId)
        );
        List<String> userIdList = contestEnrollEntityList.stream().map(ContestEnrollEntity::getUserId).collect(Collectors.toList());
        String idsList = URLEncoder.encode(JSON.toJSONString(userIdList),"UTF-8");
        ResultModel<List<UserDto>> remoteInvokeRes = userService.getUserDtoListByUserIdList(idsList);
        if(!ResultFlag.SUCCESS.equals(remoteInvokeRes.getResultFlag())){
            return ResultModel.buildFailResultModel(null,null);
        }
        List<UserDto> userDtoList = remoteInvokeRes.getData();
        Map<String, UserDto> userDtoMap = userDtoList.stream().collect(Collectors.toMap(
                UserDto::getUserId,
                userDto -> userDto
        ));
        List<OnlineStatusDto> onlineStatusDtoList = contestEnrollEntityList.stream().map(
                contestEnrollEntity -> OnlineStatusDto.buildOnlineStatusDto(
                        contestEnrollEntity,
                        userDtoMap.get(contestEnrollEntity.getUserId())
                )
        ).collect(Collectors.toList());
        return ResultModel.buildSuccessResultModel(null,onlineStatusDtoList);
    }
}
