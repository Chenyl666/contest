package com.contest.entity.question;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.contest.dto.question.QuestionRepoDto;
import com.contest.enu.QuestionRepoType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@TableName("question_repo")
public class QuestionRepoEntity {
    @TableId
    private Long questionRepoId;
    @TableField("question_repo_name")
    private String questionRepoName;
    @TableField("question_repo_type")
    private QuestionRepoType questionRepoType;
    @TableField("question_tag_id")
    private Long questionTagId;
    @TableField("created_by")
    private String createdBy;
    @TableField("created_date")
    private Date createdDate;
    @TableField("updated_date")
    private Date updatedDate;

    public QuestionRepoDto entity2Dto(){
        return QuestionRepoDto
                .builder()
                .questionRepoId(String.valueOf(questionRepoId))
                .questionRepoName(questionRepoName)
                .questionTagId(String.valueOf(questionTagId))
                .questionRepoType(questionRepoType)
                .build();
    }
}
