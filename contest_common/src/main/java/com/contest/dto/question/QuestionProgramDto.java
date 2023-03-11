package com.contest.dto.question;

import com.contest.entity.question.QuestionProgramEntity;
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
public class QuestionProgramDto {
    private String questionId;
    private String questionRepoId;
    private String questionTitle;
    private String questionContent;
    private Long timeLimit;
    private Long memoryLimit;
    private String questionInputExpress;
    private String questionOutputExpress;
    private Float score;

    @Builder.Default
    private QuestionType questionType = QuestionType.PROGRAMMING_QUESTION;

    private List<ProgramExampleDto> programExampleList;

    public QuestionProgramEntity dto2Entity(){
        return QuestionProgramEntity
                .builder()
                .questionId(questionId == null?null:Long.parseLong(questionId))
                .questionRepoId(questionRepoId == null?null:Long.parseLong(questionRepoId))
                .questionTitle(questionTitle)
                .questionContent(questionContent)
                .timeLimit(timeLimit)
                .memoryLimit(memoryLimit)
                .questionInputExpress(questionInputExpress)
                .questionOutputExpress(questionOutputExpress)
                .score(score)
                .build();
    }
}
