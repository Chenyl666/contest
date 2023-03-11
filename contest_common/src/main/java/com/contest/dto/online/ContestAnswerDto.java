package com.contest.dto.online;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.contest.dto.question.QuestionDetailDto;
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
public class ContestAnswerDto {
    private String answerId;
    private String contestId;
    private QuestionDetailDto questionDetail;
    private String answerContent;
    private String createdBy;
    private QuestionType questionType;
}
