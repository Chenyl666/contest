package com.contest.service.impl.compile;

import com.contest.dto.online.ContestCodeDto;
import com.contest.dto.question.ProgramExampleDto;
import com.contest.dto.question.QuestionProgramDto;
import com.contest.dto.user.UserDto;
import com.contest.enu.ExampleType;
import com.contest.result.ResultFlag;
import com.contest.result.ResultModel;
import com.contest.service.ProgramCompileService;
import com.contest.service.QuestionService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CCompileService implements ProgramCompileService {

    @Resource
    private QuestionService questionService;

    @Value("${compiler.windows.path}")
    private String compilerPath;

    @SneakyThrows
    @Transactional
    @Override
    public ResultModel<String> runCode(ContestCodeDto contestCodeDto, UserDto userDto) {
        ResultModel<QuestionProgramDto> questionProgramRes = questionService.getQuestionProgramById(contestCodeDto.getQuestionId());
        if(!ResultFlag.SUCCESS.equals(questionProgramRes.getResultFlag())){
            return ResultModel.buildFailResultModel();
        }

        String codePath = getCodePath(contestCodeDto.getQuestionId(), contestCodeDto.getAnswerId());
        File codeFile = buildFile(codePath, contestCodeDto.getCode());
        String exePath = getExePath(contestCodeDto.getQuestionId(), contestCodeDto.getAnswerId());
        String scriptPath = getScriptPath();


        QuestionProgramDto questionProgramDto = questionProgramRes.getData();
        Map<Integer, List<ProgramExampleDto>> programExampleMap = buildProgramExampleMap(
                questionProgramDto
        );


        return null;
    }

    public Map<Integer,List<ProgramExampleDto>> buildProgramExampleMap(QuestionProgramDto questionProgramDto){
        List<ProgramExampleDto> programExampleList = questionProgramDto.getProgramExampleList();
        List<ProgramExampleDto> inputExampleList = programExampleList.stream().filter(
                programExampleDto -> ExampleType.INPUT_EXAMPLE.equals(programExampleDto.getExampleType())
        ).collect(Collectors.toList());
        List<ProgramExampleDto> outputExampleList = programExampleList.stream().filter(
                programExampleDto -> ExampleType.OUTPUT_EXAMPLE.equals(programExampleDto.getExampleType())
        ).collect(Collectors.toList());
        Map<Integer,List<ProgramExampleDto>> programExampleMap = new HashMap<>();
        inputExampleList.forEach(inputExample -> {
            List<ProgramExampleDto> list = programExampleMap.putIfAbsent(
                    inputExample.getExampleNumber(),
                    new LinkedList<>(Collections.singletonList(inputExample))
            );
            if(list != null){
                list.add(inputExample);
            }
        });
        outputExampleList.forEach(outputExample -> {
            List<ProgramExampleDto> list = programExampleMap.putIfAbsent(
                    outputExample.getExampleNumber(),
                    new LinkedList<>(Collections.singletonList(outputExample))
            );
            if(list != null){
                list.add(outputExample);
            }
        });
        return programExampleMap;
    }

    public String getCompilerPath(){
        return this.compilerPath.concat("\\c");
    }

    public String getCodePath(Long questionId,Long answerId){
        return this.compilerPath
                .concat("\\ans\\")
                .concat(questionId.toString())
                .concat("\\")
                .concat(answerId.toString())
                .concat("Main.c");
    }

    public String getExePath(Long questionId,Long answerId){
        return this.compilerPath
                .concat("\\ans\\")
                .concat(questionId.toString())
                .concat("\\")
                .concat(answerId.toString())
                .concat("Main.exe");
    }

    public String getScriptPath(){
        return this.compilerPath.concat("\\c\\script.bat");
    }

    public File buildFile(String filePath,String code) throws IOException {
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        if(!file.exists()){
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(code);
        writer.close();
        return file;
    }

}
