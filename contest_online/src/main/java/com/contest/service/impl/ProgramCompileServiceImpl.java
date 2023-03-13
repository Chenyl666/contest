package com.contest.service.impl;

import com.contest.dto.online.ContestCodeDto;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import com.contest.service.ProgramCompileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class ProgramCompileServiceImpl implements ProgramCompileService{

    @Resource(name = "compileServiceMap")
    private Map<ContestCodeDto.LanguageType,ProgramCompileService> compileServiceMap;

    /**
     *
     * 编译运行代码*/
    @Override
    public ResultModel<String> runCode(ContestCodeDto contestCodeDto, UserDto userDto) {
        ProgramCompileService programCompileService = compileServiceMap.get(contestCodeDto.getLanguageType());
        return programCompileService.runCode(contestCodeDto, userDto);
    }
}
