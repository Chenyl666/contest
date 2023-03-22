package com.contest.service.impl;

import com.contest.dto.online.ContestCodeDto;
import com.contest.dto.user.UserDto;
import com.contest.result.ProgramResult;
import com.contest.result.ResultModel;
import com.contest.service.ProgramCompileService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ProgramCompileServiceImpl extends ProgramCompileService{

    @Resource(name = "compileServiceMap")
    private Map<String,ProgramCompileService> compileServiceMap;

    /**
     *
     * 编译运行代码*/
    @Override
    public ResultModel<List<ProgramResult>> runCode(ContestCodeDto contestCodeDto) {
        ProgramCompileService programCompileService = compileServiceMap.get(
                contestCodeDto.getLanguageType()
        );
        return programCompileService.runCode(contestCodeDto);
    }
}
