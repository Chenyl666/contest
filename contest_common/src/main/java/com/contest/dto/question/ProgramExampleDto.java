package com.contest.dto.question;

import com.baomidou.mybatisplus.annotation.TableField;
import com.contest.entity.question.ProgramExampleEntity;
import com.contest.enu.ExampleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProgramExampleDto{
    private String questionId;
    private Integer exampleNumber;
    private ExampleType exampleType;
    private String exampleUrl;

    public ProgramExampleEntity dto2Entity(){
        return ProgramExampleEntity
                .builder()
                .questionId(questionId == null? null:Long.parseLong(questionId))
                .exampleType(exampleType)
                .exampleUrl(exampleUrl)
                .exampleNumber(exampleNumber)
                .build();
    }

}
