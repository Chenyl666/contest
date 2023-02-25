package com.contest.entity.question;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.contest.dto.question.QuestionBankPaperDto;
import com.contest.enu.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@TableName("question_bank_paper")
public class QuestionBankPaperEntity {
    @TableId
    private Long questionId;
    @TableField("question_type")
    private QuestionType questionType;
    @TableField("question_content")
    private String questionContent;
    @TableField("question_ref_ans")
    private String questionReferenceAnswer;
    @TableField("question_explain")
    private String questionExplain;
    @TableField("question_repo_id")
    private Long questionRepoId;
    @TableField("created_by")
    private String createdBy;
    @TableField("created_date")
    private Date createdDate;
    @TableField("updated_date")
    private Date updatedDate;
    @TableField("score")
    private Float score;

    public QuestionBankPaperDto entity2Dto(){
        return QuestionBankPaperDto
                .builder()
                .questionId(String.valueOf(questionId))
                .questionType(questionType)
                .questionContent(questionContent)
                .questionReferenceAnswer(questionReferenceAnswer)
                .questionExplain(questionExplain)
                .questionRepoId(String.valueOf(questionRepoId))
                .createdBy(createdBy)
                .score(score)
                .build();
    }
}
