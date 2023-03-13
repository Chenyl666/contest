package com.contest.service;

import com.contest.dto.online.ContestCodeDto;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;

public interface ProgramCompileService {
    public ResultModel<String> runCode(ContestCodeDto contestCodeDto, UserDto userDto);

}
