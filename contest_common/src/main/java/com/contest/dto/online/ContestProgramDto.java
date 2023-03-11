package com.contest.dto.online;

import com.contest.dto.question.QuestionDetailDto;
import com.contest.dto.question.QuestionProgramDto;
import com.contest.enu.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ContestProgramDto {
    private String answerId;
    private String contestId;
    private QuestionProgramDto questionProgramDto;
    private String answerContent;
    private String createdBy;
    private QuestionType questionType;
}
