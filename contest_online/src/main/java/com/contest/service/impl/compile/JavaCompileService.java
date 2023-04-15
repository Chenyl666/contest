package com.contest.service.impl.compile;

import com.contest.dto.online.ContestCodeDto;
import com.contest.dto.question.QuestionProgramDto;
import com.contest.entity.online.ContestAnswerEntity;
import com.contest.mapper.ContestAnswerMapper;
import com.contest.result.ProgramResult;
import com.contest.result.ResultFlag;
import com.contest.result.ResultModel;
import com.contest.service.ProgramCompileService;
import com.contest.service.QuestionService;
import com.contest.util.FileUtils;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class JavaCompileService extends ProgramCompileService {
    @Value("${compiler.windows.prefix}")
    private String compilerEnvPath;

    @Value("${compiler.windows.input}")
    private String inputPathTemplate;

    @Value("${compiler.windows.output}")
    private String outputPathTemplate;

    @Value("${compiler.windows.answer}")
    private String answerPath;

    @Value("${compiler.windows.java}")
    private String runningScript;

    @Resource
    private QuestionService questionService;

    @Resource
    private ContestAnswerMapper contestAnswerMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 对源码进行编译运行
     * 需要提前准备好脚本文件，并且遵守以下四个参数规则：
     * 1. 第一个参数为源码路径 %1
     * 2. 第二个参数为可执行文件路径 %2
     * 3. 第三个参数为案例输入路径   %3
     * 4. 第四个参数为执行结果重定向路径  %4
     */
    @SneakyThrows
    @Transactional
    @Override
    public ResultModel<List<ProgramResult>> runCode(ContestCodeDto contestCodeDto) {
        // 远程调用获取题目数据
        ResultModel<QuestionProgramDto> res = questionService.getQuestionProgramById(contestCodeDto.getQuestionId());
        if (!ResultFlag.SUCCESS.equals(res.getResultFlag())) {
            return ResultModel.buildFailResultModel(null, null);
        }
        QuestionProgramDto questionProgramDto = res.getData();
        if (questionProgramDto.getProgramExampleList().size() % 2 == 1) {
            throw new Exception("data error!");
        }
        // 写入代码文件 生成代码文件目录参数、可执行文件目录参数、重定向文件参数
        String code = contestCodeDto.getCode();
        String codeFilePath = buildCodeFilePath(contestCodeDto.getQuestionId(), contestCodeDto.getAnswerId());
        buildFile(codeFilePath, code);
        String customerOutPath = buildCustomerOutPath(contestCodeDto.getQuestionId(), contestCodeDto.getAnswerId());
        // 生成 测试点输入文件参数、测试点输出文件参数、脚本调用命令，执行脚本并通过MD5值比较重定向文件和输出文件，最后统计分数
        int exampleCount = questionProgramDto.getProgramExampleList().size() / 2;
        float score = 0;
        List<ProgramResult> programResultList = new ArrayList<>();
        for (int i = 0; i < exampleCount; i++) {
            cleanCacheFile(contestCodeDto);
            String inputPath = buildInputPath(contestCodeDto.getQuestionId(), i);
            String outputPath = buildOutputPath(contestCodeDto.getQuestionId(), i);
            String command = getScriptCommand(
                    contestCodeDto.getQuestionId().toString(),
                    contestCodeDto.getAnswerId().toString(),
                    inputPath
            );
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(command, null, new File(compilerEnvPath));
            boolean normalTerminate = process.waitFor(questionProgramDto.getTimeLimit() * 10, TimeUnit.MILLISECONDS);
            if (!normalTerminate) {
                killTimeoutThread(contestCodeDto.getAnswerId());
                programResultList.add(ProgramResult.buildTimeout(i));
                FileUtils.deleteFile(
                        this.answerPath
                                .replace("[!1!]", contestCodeDto.getQuestionId().toString())
                                .replace("[!2!]", contestCodeDto.getAnswerId().toString())
                                .concat("\\customer.out")
                );
                String compileErrorMessage = getOutputText(contestCodeDto, "compiler_error.out");
                if (compileErrorMessage != null) {
                    return ResultModel.buildFailResultModel(compileErrorMessage, null);
                }
                continue;
            }
            String exeFilePath = buildExeFilePath(contestCodeDto.getQuestionId(), contestCodeDto.getAnswerId());
            if (getExecFileSize(exeFilePath) > questionProgramDto.getMemoryLimit() * 1024 * 1024) {
                programResultList.add(ProgramResult.buildMemoryOverFlow(i));
                FileUtils.deleteFile(
                        this.answerPath
                                .replace("[!1!]", contestCodeDto.getQuestionId().toString())
                                .replace("[!2!]", contestCodeDto.getAnswerId().toString())
                                .concat("\\customer.out")
                );
                continue;
            }
            if (FileUtils.fileEquals(outputPath, customerOutPath)) {
                String inputText = getInputText(contestCodeDto.getQuestionId(), i);
                String outputText = getOutputText(contestCodeDto, "customer.out");
                programResultList.add(ProgramResult.buildAllPass(i, 5f,inputText,outputText));
                score += 5;
            } else {
                String runtimeErrorMessage = getOutputText(contestCodeDto, "runtime_error.out");
                String compilerErrorMessage = getOutputText(contestCodeDto, "compiler_error.out");
                if (compilerErrorMessage != null) {
                    return ResultModel.buildFailResultModel(compilerErrorMessage, null);
                }
                if (runtimeErrorMessage != null) {
                    FileUtils.deleteFile(
                            this.answerPath
                                    .replace("[!1!]", contestCodeDto.getQuestionId().toString())
                                    .replace("[!2!]", contestCodeDto.getAnswerId().toString())
                                    .concat("\\runtime_error.out")
                    );
                    return ResultModel.buildFailResultModel(runtimeErrorMessage, null);
                }
                String inputText = getInputText(contestCodeDto.getQuestionId(), i);
                String outputText = getOutputText(contestCodeDto, "customer.out");
                programResultList.add(ProgramResult.buildError(i,inputText,outputText));
            }
            FileUtils.deleteFile(
                    this.answerPath
                            .replace("[!1!]", contestCodeDto.getQuestionId().toString())
                            .replace("[!2!]", contestCodeDto.getAnswerId().toString())
                            .concat("\\customer.out")
            );
        }
        clearCacheFile(contestCodeDto.getQuestionId(), contestCodeDto.getAnswerId());
        FileUtils.deleteFile(
                this.answerPath.replace("[!1!]", contestCodeDto.getQuestionId().toString())
                        .replace("[!2!]", contestCodeDto.getAnswerId().toString()).concat("\\customer.out")
        );
        // 保存较大的分数
        ContestAnswerEntity contestAnswerEntity = contestAnswerMapper.selectById(contestCodeDto.getAnswerId());
        Float sumScore = contestAnswerEntity.getScore();
        if (score > sumScore) {
            contestAnswerEntity.setAnswerContent("true");
            contestAnswerEntity.setScore(score);
            contestAnswerMapper.updateById(contestAnswerEntity);
        }
        return ResultModel.buildSuccessResultModel(null, programResultList);
    }

    public void buildFile(String filePath, String content) {
        FileUtils.writeFile(new File(filePath), content);
    }

    public String getScriptCommand(String p1, String p2, String p3) {
        return this.runningScript
                .concat(" ").concat(p1)
                .concat(" ").concat(p2)
                .concat(" ").concat(p3);
    }

    public String buildCodeFileDir(Long questionId, Long answerId) {
        return answerPath
                .replace("[!1!]", questionId.toString())
                .replace("[!2!]", answerId.toString());
    }

    public String buildCodeFilePath(Long questionId, Long answerId) {
        return answerPath
                .replace("[!1!]", questionId.toString())
                .replace("[!2!]", answerId.toString())
                .concat("\\Main.java");
    }

    public String buildCustomerOutPath(Long questionId, Long answerId) {
        return answerPath
                .replace("[!1!]", questionId.toString())
                .replace("[!2!]", answerId.toString())
                .concat("\\customer.out");
    }

    public String buildExeFilePath(Long questionId, Long answerId) {
        return answerPath
                .replace("[!1!]", questionId.toString())
                .replace("[!2!]", answerId.toString())
                .concat("\\Main.class");
    }

    public String buildInputPath(Long questionId, Integer number) {
        return inputPathTemplate
                .replace("[!1!]", questionId.toString())
                .replace("[!2!]", String.valueOf(number));
    }

    public String buildOutputPath(Long questionId, Integer number) {
        return outputPathTemplate
                .replace("[!1!]", questionId.toString())
                .replace("[!2!]", String.valueOf(number));
    }

    public Long getExecFileSize(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return 0L;
        }
        return file.length();
    }

    @SneakyThrows
    public void killTimeoutThread(Long answerId) {
        String className = "Runner".concat(answerId.toString());
        String jps = "jps -l";
        Process jpsProcess = Runtime.getRuntime().exec(jps);
        InputStreamReader in = new InputStreamReader(jpsProcess.getInputStream());
        char[] buf = new char[100];
        int len;
        StringBuilder stringBuilder = new StringBuilder();
        while ((len = in.read(buf)) != -1) {
            stringBuilder.append(buf, 0, len);
        }
        in.close();
        String output = stringBuilder.toString();
        String[] split = output.split("\n");
        int index = -1;
        String processExpress = null;
        for (String line : split) {
            index = line.indexOf(className);
            if (index != -1) {
                processExpress = line;
                break;
            }
        }
        if (index == -1) {
            return;
        }
        String pid = processExpress.split(" ")[0];
        String cmd = "taskkill /PID ".concat(pid).concat(" /F");
        Runtime.getRuntime().exec(cmd);
    }

    @SuppressWarnings("all")
    public void clearCacheFile(Long questionId, Long answerId) {
        String filePath = this.answerPath
                .replace("[!1!]", questionId.toString())
                .replace("[!2!]", answerId.toString());
        File fileDir = new File(filePath);
        deleteFileDir(fileDir);
    }

    public void deleteFileDir(File file) {
        if (!file.exists()) {
            return;
        }
        for (File subFile : Objects.requireNonNull(file.listFiles())) {
            if (subFile.isFile()) {
                subFile.delete();
            } else {
                deleteFileDir(subFile);
            }
        }
    }

    @SneakyThrows
    private String getOutputText(ContestCodeDto contestCodeDto, String errorMessageFile) {
        String compilerErrorMessageFilePath = this.answerPath
                .replace("[!1!]", contestCodeDto.getQuestionId().toString())
                .replace("[!2!]", contestCodeDto.getAnswerId().toString())
                .concat("\\")
                .concat(errorMessageFile);
        File file = new File(compilerErrorMessageFilePath);
        if (file.length() == 0 || !file.exists()) {
            return null;
        }
        InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), "GB2312");
        StringBuilder stringBuilder = new StringBuilder();
        char[] buf = new char[128];
        int len = -1;
        while ((len = reader.read(buf)) != -1) {
            stringBuilder.append(buf, 0, len);
        }
        reader.close();
        return stringBuilder.toString();
    }

    @SneakyThrows
    private String getInputText(Long questionId, Integer index) {
        Object cache = redisTemplate.opsForValue().get("PROGRAM::QUESTION::".concat(questionId.toString()));
        if (cache != null) {
            return cache.toString();
        }
        String filePath = inputPathTemplate
                .replace("[!1!]",questionId.toString())
                .replace("[!2!]",index.toString());
        File file = new File(filePath);
        InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()));
        char[] buf = new char[128];
        int len = -1;
        StringBuilder stringBuilder = new StringBuilder();
        while((len = reader.read(buf))!=-1){
            stringBuilder.append(buf,0,len);
        }
        return stringBuilder.toString();
    }

    private void cleanCacheFile(ContestCodeDto contestCodeDto){
        FileUtils.deleteFile(this.answerPath
                .replace("[!1!]",contestCodeDto.getQuestionId().toString())
                .replace("[!2!]",contestCodeDto.getAnswerId().toString())
                .concat("\\customer.out")
        );
        FileUtils.deleteFile(this.answerPath
                .replace("[!1!]",contestCodeDto.getQuestionId().toString())
                .replace("[!2!]",contestCodeDto.getAnswerId().toString())
                .concat("\\compiler_error.out")
        );
        FileUtils.deleteFile(this.answerPath
                .replace("[!1!]",contestCodeDto.getQuestionId().toString())
                .replace("[!2!]",contestCodeDto.getAnswerId().toString())
                .concat("\\runtime_error.out")
        );
    }

}
