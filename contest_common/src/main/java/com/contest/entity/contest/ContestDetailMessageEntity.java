package com.contest.entity.contest;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.contest.entity.contest.ContestLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@TableName("contest_detail_message")
public class ContestDetailMessageEntity {
    @TableId
    private String contestId;
    @TableField("contest_subject")
    private String contestSubject;
    @TableField("contest_price")
    private String contestPrice;
    @TableField("required_contest_paying")
    private boolean requiredContestPaying;
    @TableField("contest_description")
    private String contestDescription;
    @TableField("enroll_start_time")
    private Date enrollStartTime;
    @TableField("enroll_end_time")
    private Date enrollEndTime;
    @TableField("contest_start_time")
    private Date contestStartTime;
    @TableField("contest_end_time")
    private Date contestEndTime;
    @TableField("created_by")
    private String createdBy;
    @TableField("type_name")
    private String typeName;
    @TableField("contest_level")
    private ContestLevel contestLevel;
    @TableField("contest_picture")
    private String contestPicture;
    @TableField("grouping_contest")
    private Boolean groupingContest;
    @TableField("grouping_max_num")
    private Integer groupingMaxNum;
    @TableField("grouping_min_num")
    private Integer groupingMinNum;
    @TableField("organize_unit")
    private String organizeUnit;

    public void setContestId(Long contestId){
        this.contestId = String.valueOf(contestId);
    }
}
