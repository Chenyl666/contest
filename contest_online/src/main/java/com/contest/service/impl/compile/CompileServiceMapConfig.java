package com.contest.service.impl.compile;

import com.contest.dto.online.ContestCodeDto;
import com.contest.service.ProgramCompileService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class CompileServiceMapConfig {
    @Resource(type = CCompileService.class)
    private ProgramCompileService cCompileService;

    @Resource(type = JavaCompileService.class)
    private ProgramCompileService javaCompileService;

    @Bean("compileServiceMap")
    public Map<ContestCodeDto.LanguageType,ProgramCompileService> compileServiceMap(){
        HashMap<ContestCodeDto.LanguageType, ProgramCompileService> map = new HashMap<>();
        map.put(ContestCodeDto.LanguageType.c,this.cCompileService);
        map.put(ContestCodeDto.LanguageType.java,this.javaCompileService);
        return map;
    }
}
