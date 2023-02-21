package com.contest.dto.question;

import com.baomidou.mybatisplus.annotation.TableField;
import com.contest.entity.question.QuestionTagEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class QuestionTagDto {
    private String questionTagId;
    private String questionTagName;

    public QuestionTagEntity dto2Entity(){
        return QuestionTagEntity
                .builder()
                .questionTagId(Long.parseLong(questionTagId))
                .questionTagName(questionTagName)
                .build();
    }


}
