package com.contest.entity.online;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.contest.dto.online.ContestAnswerDto;
import com.contest.dto.online.ContestProgramDto;
import com.contest.dto.question.QuestionDetailDto;
import com.contest.dto.question.QuestionProgramDto;
import com.contest.enu.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@TableName("contest_answer")
public class ContestAnswerEntity {
    @TableId
    private Long answerId;
    @TableField("contest_id")
    private Long contestId;
    @TableField("question_id")
    private Long questionId;
    @TableField("ans_content")
    private String answerContent;
    @TableField("score")
    private Float score;
    @TableField("created_by")
    private String createdBy;
    @TableField("judge_comment")
    private String judgeComment;
    @TableField("question_type")
    private QuestionType questionType;
    @TableField("created_date")
    private Date createdDate;
    @TableField("updated_date")
    private Date updatedDate;

    public ContestAnswerDto entity2Dto(QuestionDetailDto questionDetailDto){
        return ContestAnswerDto
                .builder()
                .answerId(String.valueOf(answerId))
                .contestId(String.valueOf(contestId))
                .questionDetail(questionDetailDto)
                .answerContent(answerContent)
                .createdBy(createdBy)
                .questionType(questionType)
                .build();
    }

    public ContestProgramDto entity2Dto(QuestionProgramDto questionProgramDto){
        return ContestProgramDto
                .builder()
                .questionProgramDto(questionProgramDto)
                .answerContent(answerContent)
                .answerId(String.valueOf(answerId))
                .contestId(String.valueOf(contestId))
                .createdBy(createdBy)
                .questionType(questionType)
                .build();
    }

}
