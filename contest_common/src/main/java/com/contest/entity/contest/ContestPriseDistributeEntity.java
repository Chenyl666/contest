package com.contest.entity.contest;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.contest.dto.contest.ContestPriseDistributeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@TableName("contest_prise_distribute")
public class ContestPriseDistributeEntity {
    @TableField("contest_id")
    private Long contestId;
    @TableField("level")
    private int level;
    @TableField("value")
    private int value;
    @TableField("created_date")
    private Date createdDate;
    @TableField("updated_date")
    private Date updatedDate;

    public ContestPriseDistributeDto entity2Dto(){
        return ContestPriseDistributeDto
                .builder()
                .level(this.level)
                .value(this.value)
                .build();
    }
}
