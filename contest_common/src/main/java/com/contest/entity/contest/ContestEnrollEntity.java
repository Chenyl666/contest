package com.contest.entity.contest;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.contest.dto.contest.ContestEnrollDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@TableName("contest_enroll")
public class ContestEnrollEntity implements Serializable {
    @TableId
    private Long enrollId;
    @TableField("contest_id")
    private Long contestId;
    @TableField("user_id")
    private String userId;
    @TableField("pay")
    private Boolean pay;
    @TableField("created_date")
    private Date createdDate;
    @TableField("updated_date")
    private Date updatedDate;
    @TableField("status")
    private Status status;

    public static enum Status{
        NO_START,ING,END
    }

    public ContestEnrollDto entity2Dto(){
        return ContestEnrollDto
                .builder()
                .enrollId(String.valueOf(enrollId))
                .contestId(String.valueOf(contestId))
                .userId(userId)
                .pay(pay)
                .build();
    }

    public void setIngStatus(){
        this.status = Status.ING;
    }

    public void setEndStatus(){
        this.status = Status.END;
    }

    public void setNoStartStatus(){
        this.status = Status.NO_START;
    }
}
