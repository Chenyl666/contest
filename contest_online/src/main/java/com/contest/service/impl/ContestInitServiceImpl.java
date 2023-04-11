package com.contest.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.contest.dto.question.ProgramExampleDto;
import com.contest.dto.question.QuestionIndexDto;
import com.contest.dto.question.QuestionProgramDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.contest.ContestDetailEntity;
import com.contest.entity.contest.ContestEnrollEntity;
import com.contest.entity.online.ContestAnswerEntity;
import com.contest.enu.ExampleType;
import com.contest.mapper.ContestDetailMapper;
import com.contest.mapper.ContestEnrollMapper;
import com.contest.result.ResultFlag;
import com.contest.result.ResultModel;
import com.contest.service.ContestAnswerService;
import com.contest.service.ContestInitService;
import com.contest.service.QuestionService;
import com.contest.util.FileUtils;
import com.contest.util.SnowMaker;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class ContestInitServiceImpl implements ContestInitService {
    @Value("${compiler.windows.input}")
    private String inputPathTemplate;

    @Value("${compiler.windows.output}")
    private String outputPathTemplate;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private ContestDetailMapper contestDetailMapper;

    @Resource
    private QuestionService questionService;

    @Resource
    private ContestAnswerService contestAnswerService;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private ContestEnrollMapper contestEnrollMapper;

    private final Map<Long,Lock> lockMap = new ConcurrentHashMap<>();

    private static final SnowMaker snowMaker = new SnowMaker(1);

    /**
     * 初始化竞赛场次
     * */
    @Override
    @Transactional
    public ResultModel<String> initContest(UserDto userDto, Long contestId) {
        long count = contestAnswerService.count(
                new QueryWrapper<ContestAnswerEntity>()
                        .eq("contest_id", contestId)
                        .eq("created_by", userDto.getUserId())
        );
        if(count != 0){
            return ResultModel.buildSuccessResultModel();
        }
        ContestDetailEntity contestDetailEntity = contestDetailMapper.selectOne(
                new QueryWrapper<ContestDetailEntity>().eq("contest_id", contestId));
        checkProgramExampleLoad(contestDetailEntity);
        Long questionRepoId = contestDetailEntity.getQuestionRepoId();
        ResultModel<List<QuestionIndexDto>> remoteInvokeResult =
                questionService.getQuestionsByRepoId(questionRepoId);
        if(!ResultFlag.SUCCESS.equals(remoteInvokeResult.getResultFlag())){
            return ResultModel.buildFailResultModel();
        }
        List<QuestionIndexDto> answerList = remoteInvokeResult.getData();
        List<ContestAnswerEntity> contestAnswerEntityList = new LinkedList<>();
        answerList.forEach(QuestionIndexDto -> {
            ContestAnswerEntity contestAnswerEntity = ContestAnswerEntity
                    .builder()
                    .answerId(snowMaker.nextId())
                    .answerContent(null)
                    .contestId(contestId)
                    .createdBy(userDto.getUserId())
                    .questionId(Long.parseLong(QuestionIndexDto.getQuestionId()))
                    .createdDate(new Date())
                    .questionType(QuestionIndexDto.getQuestionType())
                    .score(0f)
                    .judgeComment(null)
                    .updatedDate(new Date())
                    .build();
            contestAnswerEntityList.add(contestAnswerEntity);
        });
        contestAnswerService.saveBatch(contestAnswerEntityList);
        return ResultModel.buildSuccessResultModel();
    }

    /**
     * 获取比赛权限
     * */
    @Override
    public ResultModel<String> checkContestStatus(UserDto userDto, Long contestId) {
        // 检查时间是否符合
        ContestDetailEntity contestDetailEntity = contestDetailMapper.selectById(contestId);
        if(contestDetailEntity == null) {
            return ResultModel.buildFailResultModel();
        }
        long startTimeStamp = contestDetailEntity.getContestStartTime().getTime();
        long endTimeStamp = contestDetailEntity.getContestEndTime().getTime();
        long nowTimeStamp = System.currentTimeMillis();
        if(nowTimeStamp < startTimeStamp || nowTimeStamp > endTimeStamp){
            return ResultModel.buildFailResultModel("不在竞赛时间范围内！");
        }
        // 检查用户是否已经交过卷
        ContestEnrollEntity contestEnrollEntity = contestEnrollMapper.selectOne(
                new QueryWrapper<ContestEnrollEntity>()
                        .eq("contest_id", contestDetailEntity.getContestId())
                        .eq("user_id", userDto.getUserId())
        );
        if (ContestEnrollEntity.Status.END.equals(contestEnrollEntity.getStatus())) {
            return ResultModel.buildFailResultModel("已经提交过试卷！");
        }
        return ResultModel.buildSuccessResultModel();
    }

    public void checkProgramExampleLoad(ContestDetailEntity contestDetail){
        if(!contestDetail.getContestTypeId().equals(2)){
            return;
        }
        String key = "CONTEST:PROGRAM:EXAMPLE:LOAD:".concat(contestDetail.getContestId().toString());
        Object value = redisTemplate.opsForValue().get(key);
        if(value == null){
            Lock lock = lockMap.putIfAbsent(contestDetail.getContestId(),new ReentrantLock());
            if(lock == null){
                lock = lockMap.get(contestDetail.getContestId());
            }
            lock.lock();
            try {
                if(redisTemplate.opsForValue().get(key) == null){
                    redisTemplate.opsForValue().set(key,1);
                    loadProgramExample(contestDetail);
                }
            }finally {
                lock.unlock();
            }
        }
    }

    @SneakyThrows
    public void loadProgramExample(ContestDetailEntity contestDetail){
        Long questionRepoId = contestDetail.getQuestionRepoId();
        ResultModel<List<QuestionProgramDto>> res = questionService.getQuestionProgramListByRepoId(questionRepoId);
        if(!ResultFlag.SUCCESS.equals(res.getResultFlag())){
            throw new Exception("没有数据！");
        }
        List<String> questionIdList = res.getData().stream().map(QuestionProgramDto::getQuestionId).collect(Collectors.toList());
        questionIdList.forEach(questionId -> {
            ResultModel<QuestionProgramDto> r = questionService.getQuestionProgramById(Long.parseLong(questionId));
            if(!ResultFlag.SUCCESS.equals(r.getResultFlag())){
                try {
                    throw new Exception("没有数据！");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            QuestionProgramDto questionProgramDto = r.getData();
            System.out.println(questionProgramDto == null);
            System.out.println(JSON.toJSONString(questionProgramDto));
            Map<Integer, List<ProgramExampleDto>> exampleMap = buildProgramExampleMap(questionProgramDto);
            for (Integer number : exampleMap.keySet()) {
                List<ProgramExampleDto> programExampleList = exampleMap.get(number);
                ProgramExampleDto inputExampleDto = programExampleList.get(0);
                ProgramExampleDto outputExampleDto = programExampleList.get(1);
                String inputUrl = inputExampleDto.getExampleUrl();
                String outputUrl = outputExampleDto.getExampleUrl();
                ResponseEntity<byte[]> forEntity = restTemplate.getForEntity(inputUrl, byte[].class);
                byte[] inputBuf = restTemplate.getForObject(inputUrl, byte[].class);
                byte[] outputBuf = restTemplate.getForObject(outputUrl, byte[].class);
                String inputExampleFilePath = inputPathTemplate
                        .replace("[!1!]", questionId)
                        .replace("[!2!]", number.toString());
                String outputExampleFilePath = outputPathTemplate
                        .replace("[!1!]", questionId)
                        .replace("[!2!]", number.toString());
                FileUtils.writeFile(new File(inputExampleFilePath),inputBuf);
                FileUtils.writeFile(new File(outputExampleFilePath),outputBuf);
            }
        });
    }

    public Map<Integer,List<ProgramExampleDto>> buildProgramExampleMap(QuestionProgramDto questionProgramDto){
        List<ProgramExampleDto> programExampleList = questionProgramDto.getProgramExampleList();
        List<ProgramExampleDto> inputExampleList = programExampleList.stream().filter(
                programExampleDto -> ExampleType.INPUT_EXAMPLE.equals(programExampleDto.getExampleType())
        ).collect(Collectors.toList());
        List<ProgramExampleDto> outputExampleList = programExampleList.stream().filter(
                programExampleDto -> ExampleType.OUTPUT_EXAMPLE.equals(programExampleDto.getExampleType())
        ).collect(Collectors.toList());
        Map<Integer,List<ProgramExampleDto>> programExampleMap = new HashMap<>();
        inputExampleList.forEach(inputExample -> {
            List<ProgramExampleDto> list = programExampleMap.putIfAbsent(
                    inputExample.getExampleNumber(),
                    new LinkedList<>(Collections.singletonList(inputExample))
            );
            if(list != null){
                list.add(inputExample);
            }
        });
        outputExampleList.forEach(outputExample -> {
            List<ProgramExampleDto> list = programExampleMap.putIfAbsent(
                    outputExample.getExampleNumber(),
                    new LinkedList<>(Collections.singletonList(outputExample))
            );
            if(list != null){
                list.add(outputExample);
            }
        });
        return programExampleMap;
    }
}
