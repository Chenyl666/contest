package com.contest.entity.question;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.contest.dto.question.QuestionTagDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@TableName("question_tag")
public class QuestionTagEntity {
    @TableField("question_tag_id")
    private Long questionTagId;
    @TableField("question_tag_name")
    private String questionTagName;
    @TableField("created_by")
    private String createdBy;
    @TableField("created_date")
    private Date createdDate;
    @TableField("updated_date")
    private Date updatedDate;

    public QuestionTagDto entity2Dto(){
        return QuestionTagDto
                .builder()
                .questionTagId(String.valueOf(questionTagId))
                .questionTagName(questionTagName)
                .build();
    }
}
