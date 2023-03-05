package com.contest.entity.question;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.contest.dto.question.ProgramExampleDto;
import com.contest.dto.question.QuestionProgramDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@TableName("question_program")
public class QuestionProgramEntity {
    @TableId
    private Long questionId;
    @TableField("question_repo_id")
    private Long questionRepoId;
    @TableField("question_title")
    private String questionTitle;
    @TableField("question_content")
    private String questionContent;
    @TableField("time_limit")
    private Long timeLimit;
    @TableField("question_input_express")
    private String questionInputExpress;
    @TableField("question_output_express")
    private String questionOutputExpress;
    @TableField("memory_limit")
    private Long memoryLimit;
    @TableField("created_by")
    private String createdBy;
    @TableField("created_date")
    private Date createdDate;
    @TableField("updated_date")
    @Builder.Default
    private Date updatedBy = new Date();
    @TableField("score")
    private Float score;

    public QuestionProgramDto entity2Dto(){
        return QuestionProgramDto
                .builder()
                .questionId(String.valueOf(questionId))
                .questionRepoId(String.valueOf(questionRepoId))
                .questionContent(questionContent)
                .questionTitle(questionTitle)
                .timeLimit(timeLimit)
                .memoryLimit(memoryLimit)
                .questionInputExpress(questionInputExpress)
                .questionOutputExpress(questionOutputExpress)
                .score(score)
                .build();
    }

    public QuestionProgramDto entity2Dto(List<ProgramExampleEntity> programExampleEntityList){
        List<ProgramExampleDto> programExampleDtoList = programExampleEntityList.stream().map(programExampleEntity -> ProgramExampleDto
                .builder()
                .exampleUrl(programExampleEntity.getExampleUrl())
                .exampleNumber(programExampleEntity.getExampleNumber())
                .questionId(String.valueOf(programExampleEntity.getQuestionId()))
                .exampleType(programExampleEntity.getExampleType())
                .build()).collect(Collectors.toList()
        );

        return QuestionProgramDto
                .builder()
                .questionId(String.valueOf(questionId))
                .questionContent(questionContent)
                .questionRepoId(String.valueOf(questionRepoId))
                .questionTitle(questionTitle)
                .timeLimit(timeLimit)
                .memoryLimit(memoryLimit)
                .questionInputExpress(questionInputExpress)
                .questionOutputExpress(questionOutputExpress)
                .score(score)
                .programExampleList(programExampleDtoList)
                .build();
    }
}
