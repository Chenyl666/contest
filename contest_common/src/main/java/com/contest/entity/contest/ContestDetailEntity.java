package com.contest.entity.contest;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.contest.dto.contest.ContestDetailDto;
import com.contest.dto.contest.ContestPriseDistributeDto;
import com.contest.enu.ContestLevel;
import com.contest.enu.ContestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@TableName("contest_detail")
public class ContestDetailEntity {
    @TableId
    private Long contestId;
    @TableField("contest_subject")
    private String contestSubject;
    @TableField("contest_price")
    private String contestPrice;
    @TableField("required_contest_paying")
    private Boolean requiredContestPaying;
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
    @TableField("contest_status")
    private ContestStatus contestStatus;
    @TableField("created_by")
    private String createdBy;
    @TableField("contest_type_id")
    private Integer contestTypeId;
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
    @TableField("auto_prise")
    private Boolean autoPrise;
    @TableField("use_percent")
    private Boolean usePercent;
    @TableField("created_date")
    private Date createdDate;
    @TableField("updated_date")
    private Date updatedDate;
    @TableField("organize_unit")
    private String organizeUnit;
    @TableField("question_repo_id")
    private Long questionRepoId;

    public ContestDetailDto entity2Dto(List<ContestPriseDistributeDto> contestPriseDistributeDtoList){
        return ContestDetailDto
                .builder()
                .contestId(String.valueOf(contestId))
                .contestSubject(contestSubject)
                .contestPrice(contestPrice)
                .requiredContestPaying(requiredContestPaying)
                .contestDescription(contestDescription)
                .enrollStartTime(enrollStartTime)
                .enrollEndTime(enrollEndTime)
                .contestStartTime(contestStartTime)
                .contestEndTime(contestEndTime)
                .contestStatus(contestStatus)
                .createdBy(createdBy)
                .contestTypeId(contestTypeId)
                .contestLevel(contestLevel)
                .contestPicture(contestPicture)
                .groupingContest(groupingContest)
                .groupingMaxNum(getGroupingMaxNum())
                .groupingMinNum(groupingMinNum)
                .autoPrise(autoPrise)
                .usePercent(usePercent)
                .organizeUnit(organizeUnit)
                .questionRepoId(String.valueOf(questionRepoId))
                .contestPriseDistributes(contestPriseDistributeDtoList)
                .build();
    }
}
