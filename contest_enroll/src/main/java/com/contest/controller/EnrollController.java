package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.contest.ContestDetailDto;
import com.contest.dto.contest.ContestEnrollDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.contest.ContestDetailEntity;
import com.contest.entity.contest.ContestDetailMessageEntity;
import com.contest.entity.contest.ContestEnrollMessage;
import com.contest.entity.contest.ContestType;
import com.contest.result.ResultModel;
import com.contest.service.ContestDetailMessageService;
import com.contest.service.ContestDetailService;
import com.contest.service.ContestTypeService;
import com.contest.service.EnrollService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.xml.transform.Result;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contest/enroll")
public class EnrollController {

    @Resource
    private ContestTypeService contestTypeService;

    @Resource
    private EnrollService enrollService;

    @Resource
    private ContestDetailMessageService contestDetailMessageService;

    @Resource
    private ContestDetailService contestDetailService;

    /**
     * 获取比赛类型
     * */
    @GetMapping("/type")
    public ResultModel<List<ContestType>> getContestTypeList(){
        return ResultModel.buildSuccessResultModel(null,contestTypeService.list(null));
    }

    /**
     * 保存比赛信息
     * */
    @PostMapping("/save")
    public ResultModel<Long> saveContestDetail(
            @RequestBody ContestDetailDto contestDetailDto,
            @CurrentUser UserDto userDto
    ){
        return enrollService.saveContestDetail(contestDetailDto, userDto);
    }

    /**
     * 获取用户的比赛列表
     * */
    @GetMapping("/get")
    public ResultModel<List<ContestDetailDto>> getContestDetailList(@CurrentUser UserDto userDto){
        return enrollService.getContestDetailByUser(userDto);
    }

    /**
     * 获取比赛的信息
     * */
    @GetMapping("/message/get/{contest_id}")
    public ResultModel<ContestDetailMessageEntity> getContestDetailMessageById(@PathVariable("contest_id") String contestId){
        return ResultModel.buildSuccessResultModel(null,contestDetailMessageService.getById(contestId));
    }

    /**
     * 通过比赛id获取举办方
     * */
    @GetMapping("/creator/get/{contest_id}")
    public ResultModel<UserDto> getCreatorByContestId(@PathVariable("contest_id")String contestId){
        return enrollService.getCreatorByContestId(contestId);
    }

    /**
     * 通过比赛的id获取比赛具体信息
     * */
    @GetMapping("/get/{contest_id}")
    public ResultModel<ContestDetailDto> getContestDetailById(@PathVariable("contest_id")String contestId){
        return enrollService.getContestDetailById(contestId);
    }

    /**
     * 更新比赛信息
     * */
    @PostMapping("/update")
    public ResultModel<String> updateContestDetail(@RequestBody ContestDetailDto contestDetailDto){
        return enrollService.updateContestDetail(contestDetailDto);
    }

    /**
     * 通过比赛id获取其类型
     * */
    @GetMapping("/type/{contest_id}")
    public ResultModel<ContestType> getContestTypeById(@PathVariable("contest_id")Long contestId){
        return enrollService.getContestTypeById(contestId);
    }

    /**
     * 给竞赛导入题库
     * */
    @PostMapping("/question/import")
    public ResultModel<String> importQuestionRepo(@RequestBody Map<String,Long> params){
        return enrollService.importQuestionRepo(params.get("contestId"),params.get("questionRepoId"));
    }

    /**
     * 通过参赛者id获取比赛报名情况
     * */
    @GetMapping("/situation/{contest_id}")
    public ResultModel<ContestEnrollDto> getContestEnrollDto(
            @PathVariable("contest_id") Long contestId,
            @CurrentUser UserDto userDto){
        return enrollService.getContestEnrollDto(contestId, userDto);
    }

    /**
     * 获取举办者的所有竞赛
     * */
    @GetMapping("/organizer/get")
    public ResultModel<List<ContestDetailDto>> getOrganizerEnrollList(@CurrentUser UserDto userDto){
        return contestDetailService.getOrganizerContestDetailList(userDto);
    }

    /**
     * 通过比赛id获取注册列表
     * */
    @GetMapping("/enrollList/{contest_id}")
    public ResultModel<List<ContestEnrollMessage>> getEnrollList(
            @CurrentUser UserDto userDto,
            @PathVariable("contest_id") Long contestId){
        return enrollService.getContestEnrollMessageListByCreatedBy(userDto,contestId);
    }

    @DeleteMapping("/delete/{enroll_id}")
    public ResultModel<String> deleteEnrollById(@PathVariable("enroll_id")Long enrollId,@CurrentUser UserDto userDto){
        return enrollService.deleteEnrollById(enrollId,userDto);
    }

    @GetMapping("/list/{contest_id}")
    public ResultModel<List<ContestEnrollDto>> getEnrollListByContestId(@PathVariable("contest_id")Long contestId){
        return enrollService.getContestEnrollListByContestId(contestId);
    }

    @GetMapping("/hot/list")
    public ResultModel<List<ContestDetailDto>> getHotContestDetailList(){
        return contestDetailService.getHotContestDetailList();
    }

    @PostMapping("/status/update")
    public ResultModel<String> updateStatus(@RequestParam("contestId")Long contestId){
        return contestDetailService.updateContestStatus(contestId);
    }

}
