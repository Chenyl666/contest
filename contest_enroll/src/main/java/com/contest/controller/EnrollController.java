package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.contest.ContestDetailDto;
import com.contest.dto.contest.ContestEnrollDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.contest.ContestDetailEntity;
import com.contest.entity.contest.ContestDetailMessageEntity;
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

    @GetMapping("/type")
    public ResultModel<List<ContestType>> getContestTypeList(){
        return ResultModel.buildSuccessResultModel(null,contestTypeService.list(null));
    }

    @PostMapping("/save")
    public ResultModel<Long> saveContestDetail(
            @RequestBody ContestDetailDto contestDetailDto,
            @CurrentUser UserDto userDto
    ){
        return enrollService.saveContestDetail(contestDetailDto, userDto);
    }

    @GetMapping("/get")
    public ResultModel<List<ContestDetailDto>> getContestDetailList(@CurrentUser UserDto userDto){
        return enrollService.getContestDetailByUser(userDto);
    }

    @GetMapping("/message/get/{contest_id}")
    public ResultModel<ContestDetailMessageEntity> getContestDetailMessageById(@PathVariable("contest_id") String contestId){
        return ResultModel.buildSuccessResultModel(null,contestDetailMessageService.getById(contestId));
    }

    @GetMapping("/creator/get/{contest_id}")
    public ResultModel<UserDto> getCreatorByContestId(@PathVariable("contest_id")String contestId){
        return enrollService.getCreatorByContestId(contestId);
    }

    @GetMapping("/get/{contest_id}")
    public ResultModel<ContestDetailDto> getContestDetailById(@PathVariable("contest_id")String contestId){
        return enrollService.getContestDetailById(contestId);
    }

    @PostMapping("/update")
    public ResultModel<String> updateContestDetail(@RequestBody ContestDetailDto contestDetailDto){
        return enrollService.updateContestDetail(contestDetailDto);
    }

    @GetMapping("/type/{contest_id}")
    public ResultModel<ContestType> getContestTypeById(@PathVariable("contest_id")Long contestId){
        return enrollService.getContestTypeById(contestId);
    }

    @PostMapping("/question/import")
    public ResultModel<String> importQuestionRepo(@RequestBody Map<String,Long> params){
        return enrollService.importQuestionRepo(params.get("contestId"),params.get("questionRepoId"));
    }

    @GetMapping("/situation/{contest_id}")
    public ResultModel<ContestEnrollDto> getContestEnrollDto(
            @PathVariable("contest_id") Long contestId,
            @CurrentUser UserDto userDto){
        return enrollService.getContestEnrollDto(contestId, userDto);
    }

    @GetMapping("/organizer/get")
    public ResultModel<List<ContestDetailDto>> getOrganizerEnrollList(@CurrentUser UserDto userDto){
        return contestDetailService.getOrganizerContestDetailList(userDto);
    }
}
