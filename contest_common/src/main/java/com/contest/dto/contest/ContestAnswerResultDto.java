package com.contest.dto.contest;

import com.contest.dto.question.QuestionDetailDto;
import com.contest.enu.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ContestAnswerResultDto {
    private String answerId;
    private Long contestId;
    private QuestionDetailDto questionDetailDto;
    private String answerContent;
    private Float score;
    private String judgeComment;
    private QuestionType questionType;
    private Boolean hasJudge;
}
