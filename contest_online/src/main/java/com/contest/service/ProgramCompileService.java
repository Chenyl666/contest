package com.contest.service;

import com.contest.dto.online.ContestCodeDto;
import com.contest.dto.question.ProgramExampleDto;
import com.contest.dto.question.QuestionProgramDto;
import com.contest.dto.user.UserDto;
import com.contest.result.ProgramResult;
import com.contest.result.ResultModel;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ProgramCompileService {

    public abstract ResultModel<List<ProgramResult>> runCode(ContestCodeDto contestCodeDto);

    public Map<Integer, List<ProgramExampleDto>> buildExampleMap(QuestionProgramDto questionProgramDto){
        List<ProgramExampleDto> programExampleList = questionProgramDto.getProgramExampleList();
        if(programExampleList == null){
            return new HashMap<>();
        }
        return programExampleList.stream().collect(Collectors.toMap(
                ProgramExampleDto::getExampleNumber,
                programExampleDto -> {
                    List<ProgramExampleDto> list = new LinkedList<>();
                    list.add(programExampleDto);
                    return list;
                },
                (l1, l2) -> {
                    l1.addAll(l2);
                    return l1;
                })
        );
    }


}
