package com.contest.dto.question;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.contest.entity.question.QuestionBankPaperEntity;
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
public class QuestionBankPaperDto {
    private String questionId;
    private QuestionType questionType;
    private String questionContent;
    private String questionReferenceAnswer;
    private String questionExplain;
    private String questionRepoId;
    private String createdBy;
    private Float score;

    public QuestionBankPaperEntity dto2Entity(){
        return QuestionBankPaperEntity
                .builder()
                .questionId(Long.parseLong(questionId))
                .questionType(questionType)
                .questionContent(questionContent)
                .questionReferenceAnswer(questionReferenceAnswer)
                .questionExplain(questionExplain)
                .questionRepoId(Long.parseLong(questionRepoId))
                .createdBy(createdBy)
                .score(score)
                .build();
    }
}
