package com.contest.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.contest.async.provider.NotifySendingProvider;
import com.contest.dto.contest.ContestResultDto;
import com.contest.dto.contest.ContestUserResult;
import com.contest.dto.user.UserDetailDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.contest.ContestDetailEntity;
import com.contest.entity.contest.ContestEnrollEntity;
import com.contest.entity.contest.ContestResultEntity;
import com.contest.entity.notify.NotifyMessageEntity;
import com.contest.enu.ContestStatus;
import com.contest.mapper.ContestDetailMapper;
import com.contest.mapper.ContestEnrollMapper;
import com.contest.mapper.ContestResultMapper;
import com.contest.result.ResultFlag;
import com.contest.result.ResultModel;
import com.contest.service.ContestResultService;
import com.contest.service.UserService;
import com.contest.util.SnowMaker;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContestResultServiceImpl implements ContestResultService {

    @Resource
    private ContestDetailMapper contestDetailMapper;

    @Resource
    private ContestResultMapper contestResultMapper;

    @Resource
    private UserService userService;

    @Resource
    private ContestEnrollMapper contestEnrollMapper;

    @Resource
    private NotifySendingProvider notifySendingProvider;

    private static final SnowMaker snowMaker = new SnowMaker(1);

    /**
     * 生成成绩
     * */
    @Override
    public ResultModel<String> generateContestResult(Long contestId) {
        ContestDetailEntity contestDetailEntity = contestDetailMapper.selectById(contestId);
        if(!ContestStatus.CONTEST_END.equals(contestDetailEntity.getContestStatus())){
            return ResultModel.buildFailResultModel("比赛未结束！");
        }
        contestDetailEntity.setContestStatus(ContestStatus.RESULT);
        contestDetailMapper.updateById(contestDetailEntity);
        return ResultModel.buildSuccessResultModel();
    }

    /**
     * 发布成绩
     * */
    @Override
    public ResultModel<String> publishContestResult(Long contestId) {
        ContestDetailEntity contestDetailEntity = contestDetailMapper.selectById(contestId);
        if(!ContestStatus.RESULT.equals(contestDetailEntity.getContestStatus())){
            return ResultModel.buildFailResultModel("请先评阅题目并生成成绩！");
        }
        contestDetailEntity.setContestStatus(ContestStatus.PUBLISH);
        contestDetailMapper.updateById(contestDetailEntity);
        List<ContestEnrollEntity> contestEnrollEntityList = contestEnrollMapper.selectList(
                new QueryWrapper<ContestEnrollEntity>().eq("contest_id", contestId)
        );
        contestEnrollEntityList.forEach(contestEnrollEntity -> notifySendingProvider.sendNotifyMessage(
                NotifyMessageEntity
                        .builder()
                        .hasRead(false)
                        .receiver(contestEnrollEntity.getUserId())
                        .createdDate(new Date())
                        .createdBy(contestDetailEntity.getCreatedBy())
                        .messageTitle("竞赛成绩发布通知")
                        .messageId(snowMaker.nextId())
                        .messageContent("您参加的网络竞赛".concat(contestDetailEntity.getContestSubject()).concat("已经发布成绩，请登录竞赛官网查看！"))
                        .build()
        ));
        return ResultModel.buildSuccessResultModel();
    }

    /**
     * 获取成绩列表
     * */
    @SneakyThrows
    @Override
    public ResultModel<List<ContestResultDto>> getContestResultListByContestId(Long contestId) {
        List<ContestResultEntity> contestResultEntityList = contestResultMapper.getContestResultEntity(contestId);
        for (int i = 0; i < contestResultEntityList.size(); i++) {
            contestResultEntityList.get(i).setRank(i+1);
        }
        List<String> userIdList = contestResultEntityList.stream().map(ContestResultEntity::getUserId).collect(Collectors.toList());
        String idListJsonStr = URLEncoder.encode(JSON.toJSONString(userIdList), "UTF-8");
        ResultModel<List<UserDto>> result = userService.getUserDtoListByUserIdList(idListJsonStr);
        if(!ResultFlag.SUCCESS.equals(result.getResultFlag())){
            return ResultModel.buildFailResultModel(null,null);
        }
        List<UserDto> userDtoList = result.getData();
        Map<String, UserDto> userDtoMap = userDtoList.stream()
                .collect(Collectors.toMap(UserDto::getUserId, userDto -> userDto));
        List<ContestResultDto> contestResultDtoList = contestResultEntityList.stream().map(contestResultEntity -> {
            ContestResultDto contestResultDto = contestResultEntity.entity2Dto();
            contestResultDto.setUserName(userDtoMap.get(contestResultDto.getUserId()).getUserName());
            return contestResultDto;
        }).collect(Collectors.toList());
        return ResultModel.buildSuccessResultModel(null,contestResultDtoList);
    }

    /**
     * 导出成绩名单
     * */
    @SneakyThrows
    @Override
    public void exportContestResultAsExcel(Long contestId, HttpServletResponse response) {
        ResultModel<List<ContestResultDto>> res = getContestResultListByContestId(contestId);
        if(ResultFlag.SUCCESS.equals(res.getResultFlag())){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(String.valueOf(System.currentTimeMillis() + ".xlsx"), "UTF-8"));
            List<ContestResultDto> data = res.getData();
            EasyExcel.write(response.getOutputStream(),ContestResultDto.class).sheet("成绩单").doWrite(data);
        }
    }

    /**
     * 获取用户的竞赛成绩
     * */
    @Override
    public ResultModel<ContestUserResult> getContestUserResult(Long contestId, UserDto userDto) {
        ResultModel<UserDetailDto> userDetail = userService.getUserDetailById(userDto.getUserId());
        if(!ResultFlag.SUCCESS.equals(userDetail.getResultFlag())){
            return ResultModel.buildFailResultModel("no exist",null);
        }
        UserDetailDto userDetailDto = userDetail.getData();
        List<ContestResultEntity> contestResultEntityList = contestResultMapper.getContestResultEntity(contestId);
        for (int i = 0; i < contestResultEntityList.size(); i++) {
            contestResultEntityList.get(i).setRank(i+1);
        }
        List<ContestResultEntity> list = contestResultEntityList.stream().filter(
                contestResultEntity -> contestResultEntity.getUserId().equals(userDetailDto.getUserId())
        ).collect(Collectors.toList());
        if(list.size() == 0){
            return ResultModel.buildFailResultModel("no exist",null);
        }
        ContestResultEntity contestResultEntity = list.get(0);
        ContestUserResult contestUserResult = ContestUserResult
                .builder()
                .contestId(contestResultEntity.getContestId().toString())
                .contestSubject(contestResultEntity.getContestSubject())
                .userId(userDetailDto.getUserId())
                .identify(userDetailDto.getIdentify())
                .rank(contestResultEntity.getRank())
                .userName(userDetailDto.getUserName())
                .sumScore(contestResultEntity.getSumScore())
                .unit(userDetailDto.getUnit())
                .build();
        return ResultModel.buildSuccessResultModel(null,contestUserResult);
    }
}
