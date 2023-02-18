package com.contest.controller;

import com.alibaba.fastjson2.JSON;
import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.contest.ContestDetailDto;
import com.contest.dto.user.UserDto;
import com.contest.entity.contest.ContestDetailEntity;
import com.contest.entity.contest.ContestType;
import com.contest.result.ResultModel;
import com.contest.service.ContestTypeService;
import com.contest.service.EnrollService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/contest/enroll")
public class EnrollController {

    @Resource
    private ContestTypeService contestTypeService;

    @Resource
    private EnrollService enrollService;

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

}
