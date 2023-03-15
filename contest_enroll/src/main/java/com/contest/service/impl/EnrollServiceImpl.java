package com.contest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.contest.async.provider.NotifySendingProvider;
import com.contest.dto.contest.ContestDetailDto;
import com.contest.dto.contest.ContestEnrollDto;
import com.contest.dto.contest.ContestPriseDistributeDto;
import com.contest.dto.question.QuestionRepoDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.contest.*;
import com.contest.entity.notify.NotifyMessageEntity;
import com.contest.enu.ContestStatus;
import com.contest.enu.QuestionRepoType;
import com.contest.enu.UserType;
import com.contest.mapper.ContestEnrollMapper;
import com.contest.mapper.ContestPriseDistributeMapper;
import com.contest.mapper.ContestTypeMapper;
import com.contest.result.ResultModel;
import com.contest.service.*;
import com.contest.util.SnowMaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
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

    @Resource
    private UserService userService;

    @Resource
    private ContestTypeMapper contestTypeMapper;

    @Resource
    private QuestionRepoService questionRepoService;

    @Resource
    private ContestEnrollMapper contestEnrollMapper;

    @Resource
    private NotifySendingProvider notifySendingProvider;

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
        sentEnrollSuccessNotifyMessage(contestDetailEntity);
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

    @Override
    public ResultModel<UserDto> getCreatorByContestId(String contestId) {
        Optional<ContestDetailEntity> contestDetailEntityOptional = Optional.ofNullable(contestDetailService.getById(contestId));
        if(contestDetailEntityOptional.isPresent()){
            return userService.getUserById(contestDetailEntityOptional.get().getCreatedBy());
        }
        return ResultModel.buildFailResultModel("no exists",null);
    }

    @Override
    public ResultModel<ContestDetailDto> getContestDetailById(String contestId) {
        ContestDetailEntity contestDetailEntity = contestDetailService.getById(contestId);
        List<ContestPriseDistributeEntity> contestPriseDistributeEntityList = contestPriseDistributeService.list(
                new QueryWrapper<ContestPriseDistributeEntity>().eq("contest_id", contestId)
        );
        List<ContestPriseDistributeDto> contestPriseDistributeDtoList = contestPriseDistributeEntityList.stream().map(
                contestPriseDistributeEntity -> ContestPriseDistributeDto
                        .builder()
                        .value(contestPriseDistributeEntity.getValue())
                        .level(contestPriseDistributeEntity.getLevel())
                        .build()
        ).collect(Collectors.toList());
        return ResultModel.buildSuccessResultModel(null,buildContestDetailDto(contestDetailEntity,contestPriseDistributeDtoList));
    }

    @Override
    public ResultModel<String> updateContestDetail(ContestDetailDto contestDetailDto) {
        contestDetailService.updateById(contestDetailDto.dto2Entity());
        return ResultModel.buildSuccessResultModel();
    }

    @Override
    public ResultModel<ContestType> getContestTypeById(Long contestId) {
        ContestDetailEntity contestDetailEntity = contestDetailService.getById(contestId);
        if(contestDetailEntity != null){
            ContestType contestType = contestTypeMapper.selectById(contestDetailEntity.getContestTypeId());
            return ResultModel.buildSuccessResultModel(null,contestType);
        }
        return ResultModel.buildFailResultModel("not exist",null);
    }

    @Override
    @Transactional
    public ResultModel<String> importQuestionRepo(Long contestId, Long questionRepoId) {
        Optional<QuestionRepoDto> questionRepoDtoOptional = Optional.ofNullable(questionRepoService.getQuestionRepoMessageById(questionRepoId).getData());
        if (!questionRepoDtoOptional.isPresent()) {
            return ResultModel.buildFailResultModel("该题库不存在！");
        }
        QuestionRepoDto questionRepoDto = questionRepoDtoOptional.get();
        Integer contestTypeId = QuestionRepoType.getContestTypeId(questionRepoDto.getQuestionRepoType());
        ResultModel<ContestDetailDto> contestDetailDtoResult = getContestDetailById(String.valueOf(contestId));
        if(contestDetailDtoResult.getData() == null){
            throw new NullPointerException();
        }
        ContestDetailDto contestDetailDto = contestDetailDtoResult.getData();
        if(!contestDetailDto.getContestTypeId().equals(contestTypeId)){
            return ResultModel.buildFailResultModel("竞赛类型和题库类型不匹配！");
        }
        contestDetailService.updateById(ContestDetailEntity
                .builder()
                .contestId(contestId)
                .questionRepoId(questionRepoId)
                .build()
        );
        return ResultModel.buildSuccessResultModel();
    }

    @Override
    public ResultModel<ContestEnrollDto> getContestEnrollDto(Long contestId, UserDto userDto) {
        ContestEnrollEntity contestEnrollEntity = contestEnrollMapper.selectOne(
                new QueryWrapper<ContestEnrollEntity>()
                        .eq("contest_id", contestId)
                        .eq("user_id", userDto.getUserId())
        );
        if(contestEnrollEntity != null){
            return ResultModel.buildSuccessResultModel(null,contestEnrollEntity.entity2Dto());
        }
        return ResultModel.buildFailResultModel("not exist",null);
    }

    @Override
    public ResultModel<List<ContestEnrollMessage>> getContestEnrollMessageListByCreatedBy(UserDto userDto,Long contestId) {
        List<ContestEnrollMessage> contestEnrollMessageList = contestEnrollMapper.getContestEnrollMessageByCreatedBy(userDto.getUserId(),contestId);
        return ResultModel.buildSuccessResultModel(null,contestEnrollMessageList);
    }

    @Override
    public ResultModel<String> deleteEnrollById(Long enrollId,UserDto userDto) {
        ContestEnrollEntity contestEnrollEntity = contestEnrollMapper.selectById(enrollId);
        Long contestId = contestEnrollEntity.getContestId();
        ContestDetailEntity contestDetailEntity = contestDetailService.getById(contestId);
        String userId = contestEnrollEntity.getUserId();
        NotifyMessageEntity notifyMessageEntity = NotifyMessageEntity
                .builder()
                .receiver(userId)
                .hasRead(false)
                .createdBy(userDto.getUserId())
                .messageTitle("参赛资格取消提醒")
                .messageContent(
                        "亲爱的参赛者，很抱歉您在已经报名的竞赛"
                                .concat(contestDetailEntity.getContestSubject())
                                .concat("由于不可控原因而取消了比赛资格，若支付过报名费，将在两个工作日内退还！")
                )
                .createdDate(new Date())
                .messageId(snowMaker.nextId())
                .build();
        notifySendingProvider.sendNotifyMessage(notifyMessageEntity);
        contestEnrollMapper.deleteById(enrollId);
        return ResultModel.buildSuccessResultModel();
    }

    private ContestDetailDto buildContestDetailDto(
            ContestDetailEntity contestDetailEntity,
            List<ContestPriseDistributeDto> contestPriseDistributeDtoList
    ){
        return ContestDetailDto
                .builder()
                .contestId(String.valueOf(contestDetailEntity.getContestId()))
                .contestSubject(contestDetailEntity.getContestSubject())
                .contestPrice(contestDetailEntity.getContestPrice())
                .requiredContestPaying(contestDetailEntity.getRequiredContestPaying())
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
                .contestPriseDistributes(contestPriseDistributeDtoList)
                .organizeUnit(contestDetailEntity.getOrganizeUnit())
                .questionRepoId(String.valueOf(contestDetailEntity.getQuestionRepoId()))
                .build();
    }

    private void sentEnrollSuccessNotifyMessage(ContestDetailEntity contestDetailEntity){
        NotifyMessageEntity notifyMessageEntity = NotifyMessageEntity
                .builder()
                .messageId(snowMaker.nextId())
                .messageTitle("审核通知")
                .messageContent(
                        "恭喜您，您举办的网络竞赛".concat(contestDetailEntity.getContestSubject())
                                .concat("已经提交成功，并且将会在两个工作日内进行受理！")
                )
                .hasRead(false)
                .receiver(contestDetailEntity.getCreatedBy())
                .createdDate(new Date())
                .createdBy("system")
                .build();
        notifySendingProvider.sendNotifyMessage(notifyMessageEntity);
    }

    private List<ContestDetailDto> buildContestDetailDtoList(
            List<ContestDetailEntity> contestDetailEntityList,
            Map<Long, List<ContestPriseDistributeDto>> contestPriseDistributeMap){
        return contestDetailEntityList.stream().map(
                contestDetailEntity -> buildContestDetailDto(
                        contestDetailEntity,contestPriseDistributeMap.get(contestDetailEntity.getContestId())
                )
        ).collect(Collectors.toList());
    }

    private void supplementContestDetailDto(ContestDetailDto contestDetailDto,UserDto userDto,Long contestId){
        contestDetailDto.setCreatedBy(userDto.getUserId());
        contestDetailDto.setContestStatus(ContestStatus.CHECKING);
        contestDetailDto.setContestId(String.valueOf(contestId));
        contestDetailDto.setCreatedBy(userDto.getUserId());
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
