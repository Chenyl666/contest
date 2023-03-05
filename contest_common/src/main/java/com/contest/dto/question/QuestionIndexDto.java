package com.contest.dto.question;

import com.contest.entity.question.QuestionDetailEntity;
import com.contest.entity.question.QuestionProgramEntity;
import com.contest.enu.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class QuestionIndexDto {
    private String questionId;
    private QuestionType questionType;
    private Float score;
    private String questionContent;

    public static QuestionIndexDto buildQuestionIndexDto(QuestionDetailEntity questionDetailEntity){
        return QuestionIndexDto
                .builder()
                .questionId(String.valueOf(questionDetailEntity.getQuestionId()))
                .questionType(questionDetailEntity.getQuestionType())
                .score(questionDetailEntity.getScore())
                .questionContent(questionDetailEntity.getQuestionContent())
                .build();
    }

    public static QuestionIndexDto buildQuestionIndexDto(QuestionProgramEntity questionProgramEntity){
        return QuestionIndexDto
                .builder()
                .questionId(String.valueOf(questionProgramEntity.getQuestionId()))
                .questionType(QuestionType.PROGRAMMING_QUESTION)
                .score(questionProgramEntity.getScore())
                .questionContent(questionProgramEntity.getQuestionTitle())
                .build();
    }
}
