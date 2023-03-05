package com.contest.entity.question;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.contest.dto.question.ProgramExampleDto;
import com.contest.enu.ExampleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@TableName("program_example")
public class ProgramExampleEntity {
    @TableField("question_id")
    private Long questionId;
    @TableField("example_number")
    private Integer exampleNumber;
    @TableField("example_type")
    private ExampleType exampleType;
    @TableField("example_url")
    private String exampleUrl;

    public ProgramExampleDto entity2Dto(){
        return ProgramExampleDto
                .builder()
                .questionId(String.valueOf(questionId))
                .exampleNumber(exampleNumber)
                .exampleType(exampleType)
                .exampleUrl(exampleUrl)
                .build();
    }

}
