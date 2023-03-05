package com.contest.entity.question;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.contest.dto.question.QuestionDetailDto;
import com.contest.enu.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@TableName("question_detail")
public class QuestionDetailEntity {
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
    @TableField("question_option")
    private String questionOption;

    public QuestionDetailDto entity2Dto(){
        return QuestionDetailDto
                .builder()
                .questionId(String.valueOf(questionId))
                .questionType(questionType)
                .questionContent(questionContent)
                .questionReferenceAnswer(questionReferenceAnswer)
                .questionExplain(questionExplain)
                .questionRepoId(String.valueOf(questionRepoId))
                .createdBy(createdBy)
                .score(score)
                .questionOption(
                        JSON.<List<QuestionDetailDto.QuestionOption>>parseObject(
                                questionOption, List.class
                        )
                )
                .build();
    }
}
