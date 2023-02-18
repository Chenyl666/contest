package com.contest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.contest.dto.contest.ContestDetailDto;
import com.contest.dto.contest.ContestPriseDistributeDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.contest.ContestDetailEntity;
import com.contest.entity.contest.ContestPriseDistributeEntity;
import com.contest.entity.contest.ContestStatus;
import com.contest.entity.user.UserType;
import com.contest.mapper.ContestPriseDistributeMapper;
import com.contest.result.ResultModel;
import com.contest.util.SnowMaker;
import com.contest.service.ContestDetailService;
import com.contest.service.ContestPriseDistributeService;
import com.contest.service.EnrollService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EnrollServiceImpl implements EnrollService {

    @Resource
    private SnowMaker snowMaker;

    @Resource
    private ContestDetailService contestDetailService;

    @Resource
    private ContestPriseDistributeService contestPriseDistributeService;

    @Resource
    private ContestPriseDistributeMapper contestPriseDistributeMapper;

    @Override
    @Transactional
    public ResultModel<Long> saveContestDetail(ContestDetailDto contestDetailDto, UserDto userDto) {
        supplementContestDetailDto(contestDetailDto,userDto,snowMaker.nextId());
        ContestDetailEntity contestDetailEntity = contestDetailDto.dto2Entity();
        contestDetailEntity.setCreatedDate(new Date());
        List<ContestPriseDistributeEntity> contestPriseDistributeEntityList = generateContestPriseDistributeEntityList(
                contestDetailDto.getContestPriseDistributes(),contestDetailEntity
        );
        contestDetailService.save(contestDetailEntity);
        contestPriseDistributeService.saveBatch(contestPriseDistributeEntityList);
        return ResultModel.buildSuccessResultModel("save complete！",contestDetailEntity.getContestId());
    }

    @Override
    public ResultModel<List<ContestDetailDto>> getContestDetailByUser(UserDto userDto) {
        List<ContestDetailDto> contestDetailDtoList;
        if(userDto.getUserType().equals(UserType.ORGANIZER)){
            List<ContestDetailEntity> contestDetailEntityList = contestDetailService.list(
                    new QueryWrapper<ContestDetailEntity>()
                            .eq("created_by", userDto.getUserId())
            );
            List<Long> contestIdList = contestDetailEntityList.stream().map(ContestDetailEntity::getContestId).collect(Collectors.toList());
            List<ContestPriseDistributeEntity> contestPriseDistributeEntityList = contestPriseDistributeMapper.selectList(
                    new QueryWrapper<ContestPriseDistributeEntity>().in("contest_id", contestIdList)
            );
            Map<Long, List<ContestPriseDistributeDto>> contestPriseDistributeMap = contestPriseDistributeEntityList.stream().collect(Collectors.toMap(
                    ContestPriseDistributeEntity::getContestId,
                    contestPriseDistributeEntity -> {
                        List<ContestPriseDistributeDto> contestPriseDistributeDtoList = new ArrayList<>();
                        contestPriseDistributeDtoList.add(contestPriseDistributeEntity.entity2Dto());
                        return contestPriseDistributeDtoList;
                    },
                    (List<ContestPriseDistributeDto> l1, List<ContestPriseDistributeDto> l2) -> {
                        l1.addAll(l2);
                        return l1;
                    }
            ));
            contestDetailDtoList = buildContestDetailDtoList(contestDetailEntityList,contestPriseDistributeMap);
        }else{
            contestDetailDtoList = new ArrayList<>();
        }
        return ResultModel.buildSuccessResultModel(null,contestDetailDtoList);
    }

    private List<ContestDetailDto> buildContestDetailDtoList(
            List<ContestDetailEntity> contestDetailEntityList,
            Map<Long, List<ContestPriseDistributeDto>> contestPriseDistributeMap
    ){
        return contestDetailEntityList.stream().map(
                contestDetailEntity -> ContestDetailDto
                .builder()
                .contestId(contestDetailEntity.getContestId())
                .contestSubject(contestDetailEntity.getContestSubject())
                .contestPrice(contestDetailEntity.getContestPrice())
                .requiredContestPaying(contestDetailEntity.isRequiredContestPaying())
                .contestDescription(contestDetailEntity.getContestDescription())
                .enrollStartTime(contestDetailEntity.getEnrollStartTime())
                .enrollEndTime(contestDetailEntity.getEnrollEndTime())
                .contestStartTime(contestDetailEntity.getContestStartTime())
                .contestEndTime(contestDetailEntity.getContestEndTime())
                .contestStatus(contestDetailEntity.getContestStatus())
                .createdBy(contestDetailEntity.getCreatedBy())
                .contestTypeId(contestDetailEntity.getContestTypeId())
                .contestLevel(contestDetailEntity.getContestLevel())
                .contestPicture(contestDetailEntity.getContestPicture())
                .groupingContest(contestDetailEntity.getGroupingContest())
                .groupingMaxNum(contestDetailEntity.getGroupingMaxNum())
                .groupingMinNum(contestDetailEntity.getGroupingMinNum())
                .autoPrise(contestDetailEntity.getAutoPrise())
                .usePercent(contestDetailEntity.getUsePercent())
                .contestPriseDistributes(contestPriseDistributeMap.get(contestDetailEntity.getContestId()))
                .build()
        ).collect(Collectors.toList());
    }

    private void supplementContestDetailDto(ContestDetailDto contestDetailDto,UserDto userDto,Long contestId){
        contestDetailDto.setCreatedBy(userDto.getUserId());
        contestDetailDto.setContestStatus(ContestStatus.CHECKING);
        contestDetailDto.setContestId(contestId);
    }

    private List<ContestPriseDistributeEntity> generateContestPriseDistributeEntityList(
            List<ContestPriseDistributeDto> contestPriseDistributes,
            ContestDetailEntity contestDetailEntity
    ){
        return contestPriseDistributes.stream().map(contestPriseDistributeDto -> ContestPriseDistributeEntity
                .builder()
                .contestId(contestDetailEntity.getContestId())
                .level(contestPriseDistributeDto.getLevel())
                .value(contestPriseDistributeDto.getValue())
                .createdDate(new Date())
                .build()
        ).collect(Collectors.toList());
    }
}
