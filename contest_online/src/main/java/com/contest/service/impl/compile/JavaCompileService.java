package com.contest.service.impl.compile;

import com.contest.dto.online.ContestCodeDto;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import com.contest.service.ProgramCompileService;
import org.springframework.stereotype.Service;

@Service
public class JavaCompileService implements ProgramCompileService {
    @Override
    public ResultModel<String> runCode(ContestCodeDto contestCodeDto, UserDto userDto) {
        return null;
    }
}
