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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContestDetailServiceImpl extends ServiceImpl<ContestDetailMapper, ContestDetailEntity> implements ContestDetailService {

    @Resource
    TaskExecutor taskExecutor;

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

    @Override
    public ResultModel<List<ContestDetailDto>> getOrganizerContestDetailList(UserDto userDto) {
        List<ContestDetailEntity> contestDetailEntityList = baseMapper.getContestDetailEntityListByOrganizerUserId(userDto.getUserId());
        List<ContestDetailDto> contestDetailDtoList = contestDetailEntityList.stream().map(contestDetailEntity -> contestDetailEntity.entity2Dto(null)).collect(Collectors.toList());
        return ResultModel.buildSuccessResultModel(null,contestDetailDtoList);
    }
}
