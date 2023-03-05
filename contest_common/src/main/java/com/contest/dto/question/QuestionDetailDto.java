package com.contest.dto.question;

import com.alibaba.fastjson2.JSON;
import com.contest.entity.question.QuestionDetailEntity;
import com.contest.enu.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class QuestionDetailDto {
    private String questionId;
    private QuestionType questionType;
    private String questionContent;
    private String questionReferenceAnswer;
    private String questionExplain;
    private String questionRepoId;
    private String createdBy;
    private Float score;
    private List<QuestionOption> questionOption;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionOption{
        private String option;
        private String content;
    }

    public QuestionDetailEntity dto2Entity(){
        return QuestionDetailEntity
                .builder()
                .questionId(Long.parseLong(questionId))
                .questionType(questionType)
                .questionContent(questionContent)
                .questionReferenceAnswer(questionReferenceAnswer)
                .questionExplain(questionExplain)
                .questionRepoId(Long.parseLong(questionRepoId))
                .createdBy(createdBy)
                .score(score)
                .questionOption(JSON.toJSONString(questionOption))
                .build();
    }
}
