package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.online.ContestCodeDto;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProgramCompileController {
    public ResultModel<String> runCode(
            @RequestBody ContestCodeDto contestCodeDto,
            @CurrentUser UserDto userDto){
        return null;
    }
}
