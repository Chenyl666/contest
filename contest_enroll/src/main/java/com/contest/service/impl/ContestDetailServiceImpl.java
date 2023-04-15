package com.contest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.contest.async.pool.TaskExecutor;
import com.contest.dto.contest.ContestDetailDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.contest.ContestDetailEntity;
import com.contest.enu.ContestStatus;
import com.contest.mapper.ContestDetailMapper;
import com.contest.result.ResultModel;
import com.contest.service.ContestDetailService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContestDetailServiceImpl extends ServiceImpl<ContestDetailMapper, ContestDetailEntity> implements ContestDetailService {

    @Resource
    private TaskExecutor taskExecutor;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 定时更新比赛状态
     * */
    @Scheduled(cron = "0 45 * * * *")
    public void scheduledUpdateContestStatus(){
        Date startTime = new Date(System.currentTimeMillis());
        startTime.setHours(startTime.getHours()+1);
        startTime.setMinutes(0);
        startTime.setSeconds(0);
        Date endTime = new Date(startTime.getTime());
        endTime.setHours(startTime.getHours()+1);
        List<ContestDetailEntity> enrollList = list(
                new QueryWrapper<ContestDetailEntity>()
                        .eq("contest_status",ContestStatus.PASSABLE)
                        .ge("enroll_start_time",startTime)
                        .le("enroll_start_time",endTime)
                        .or()
                        .eq("contest_status",ContestStatus.ENROLLING)
                        .ge("enroll_end_time",startTime)
                        .le("enroll_end_time",endTime)
                        .or()
                        .eq("contest_status",ContestStatus.ENROLL_END)
                        .ge("contest_start_time",startTime)
                        .le("contest_start_time",endTime)
                        .or()
                        .eq("contest_status",ContestStatus.CONTESTING)
                        .ge("contest_end_time",startTime)
                        .le("contest_end_time",endTime)
        );
        enrollList.forEach(this::setContestStatus);
    }

    public void setContestStatus(ContestDetailEntity contestDetailEntity) {
        Date runDate;
        switch (contestDetailEntity.getContestStatus()){
            case PASSABLE:{
                runDate = contestDetailEntity.getEnrollStartTime();
                break;
            }
            case ENROLLING:{
                runDate = contestDetailEntity.getEnrollEndTime();
                break;
            }
            case ENROLL_END:{
                runDate = contestDetailEntity.getContestStartTime();
                break;
            }
            case CONTESTING: {
                runDate = contestDetailEntity.getContestEndTime();
                break;
            }
            default: {
                try {
                    throw new Exception("status exception!");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        taskExecutor.addTask(() -> {
            contestDetailEntity.setContestStatus(contestDetailEntity.getContestStatus().getNextStatus());
        },runDate);
    }

    /**
     * 获取举办方所举办的全部比赛
     * */
    @Override
    public ResultModel<List<ContestDetailDto>> getOrganizerContestDetailList(UserDto userDto) {
        List<ContestDetailEntity> contestDetailEntityList = baseMapper.getContestDetailEntityListByOrganizerUserId(userDto.getUserId());
        List<ContestDetailDto> contestDetailDtoList = contestDetailEntityList.stream().map(contestDetailEntity -> contestDetailEntity.entity2Dto(null)).collect(Collectors.toList());
        contestDetailDtoList = contestDetailDtoList.stream().sorted(ContestDetailDto.comparator()).collect(Collectors.toList());
        return ResultModel.buildSuccessResultModel(null,contestDetailDtoList);
    }

    /**
     * 获取热点比赛数据
     * */
    @Override
    public ResultModel<List<ContestDetailDto>> getHotContestDetailList() {
        Date now = new Date();
        List<ContestDetailEntity> contestDetailEntityList = list(
                new QueryWrapper<ContestDetailEntity>()
                        .le("enroll_start_time",now)
                        .ge("enroll_end_time",now)
                        .orderByDesc("created_date")
        );
        if(contestDetailEntityList.size() == 0){
            return ResultModel.buildSuccessResultModel(null,new ArrayList<>());
        }
        contestDetailEntityList = contestDetailEntityList.subList(0, Math.min(contestDetailEntityList.size(), 10));
        List<ContestDetailDto> contestDetailDtoList = contestDetailEntityList.stream().map(
                contestDetailEntity -> contestDetailEntity.entity2Dto(null)
        ).collect(Collectors.toList());
        return ResultModel.buildSuccessResultModel(null,contestDetailDtoList);
    }

    /**
     * 更新比赛状态
     * */
    @Override
    public ResultModel<String> updateContestStatus(Long contestId) {
        ContestDetailEntity contestDetailEntity = getById(contestId);
        if(contestDetailEntity == null){
            return ResultModel.buildFailResultModel();
        }
        Date now = new Date();
        switch (contestDetailEntity.getContestStatus()){
            case PASSABLE:{
                Date enrollStartTime = contestDetailEntity.getEnrollStartTime();
                Date enrollEndTime = contestDetailEntity.getEnrollEndTime();
                if(datetimeRangeEquals(now,enrollStartTime,enrollEndTime)){
                    contestDetailEntity.setContestStatus(ContestStatus.ENROLLING);
                }
                break;
            }
            case ENROLLING:{
                Date enrollEndTime = contestDetailEntity.getEnrollEndTime();
                if(now.getTime() > enrollEndTime.getTime()){
                    contestDetailEntity.setContestStatus(ContestStatus.ENROLL_END);
                }
                break;
            }
            case ENROLL_END:{
                Date contestStartTime = contestDetailEntity.getContestStartTime();
                Date contestEndTime = contestDetailEntity.getContestEndTime();
                if(datetimeRangeEquals(now,contestStartTime,contestEndTime)){
                    contestDetailEntity.setContestStatus(ContestStatus.CONTESTING);
                }
                break;
            }
            case CONTESTING: {
                Date contestEndTime = contestDetailEntity.getContestEndTime();
                if(now.getTime() > contestEndTime.getTime()){
                    contestDetailEntity.setContestStatus(ContestStatus.CONTEST_END);
                }
                break;
            }
        }
        updateById(contestDetailEntity);
        return ResultModel.buildSuccessResultModel();
    }

    private boolean datetimeRangeEquals(Date now,Date startTime,Date endTime){
        long nowTimeStamp = now.getTime();
        long startTimeStamp = startTime.getTime();
        long endTimeStamp = endTime.getTime();
        return nowTimeStamp >= startTimeStamp && nowTimeStamp <= endTimeStamp;
    }
}
