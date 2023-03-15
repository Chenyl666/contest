package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.online.ContestCodeDto;
import com.contest.dto.user.UserDto;
import com.contest.result.ProgramResult;
import com.contest.result.ResultModel;
import com.contest.service.impl.ProgramCompileServiceImpl;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/contest/program")
public class ProgramCompileController {

    @Resource
    private ProgramCompileServiceImpl programCompileService;

    @RequestMapping("/submit")
    public ResultModel<List<ProgramResult>> runCode(
            @RequestBody ContestCodeDto contestCodeDto
    ){
        return programCompileService.runCode(contestCodeDto);
    }
}
