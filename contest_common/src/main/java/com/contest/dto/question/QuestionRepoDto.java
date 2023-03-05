package com.contest.dto.question;

import com.baomidou.mybatisplus.annotation.TableField;
import com.contest.entity.question.QuestionRepoEntity;
import com.contest.enu.QuestionRepoType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class QuestionRepoDto {
    private String questionRepoId;
    private String questionRepoName;
    private String questionTagId;
    private QuestionRepoType questionRepoType;

    public QuestionRepoEntity dto2Entity(){
        return QuestionRepoEntity
                .builder()
                .questionRepoId(Long.parseLong(questionRepoId))
                .questionRepoName(questionRepoName)
                .questionTagId(questionTagId == null?null:Long.parseLong(questionTagId))
                .questionRepoType(questionRepoType)
                .build();
    }
}
