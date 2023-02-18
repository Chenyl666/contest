package com.contest.dto.contest;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.contest.entity.contest.ContestDetailEntity;
import com.contest.entity.contest.ContestLevel;
import com.contest.entity.contest.ContestStatus;
import com.contest.util.DateUtils;
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
public class ContestDetailDto {
    private Long contestId;
    private String contestSubject;
    private String contestPrice;
    private boolean requiredContestPaying;
    private String contestDescription;
    private Date enrollStartTime;
    private Date enrollEndTime;
    private Date contestStartTime;
    private Date contestEndTime;
    private ContestStatus contestStatus;
    private String createdBy;
    private Integer contestTypeId;
    private ContestLevel contestLevel;
    private String contestPicture;
    private Boolean groupingContest;
    private Integer groupingMaxNum;
    private Integer groupingMinNum;
    private Boolean autoPrise;
    private Boolean usePercent;
    private List<ContestPriseDistributeDto> contestPriseDistributes;

    public void setEnrollStartTime(String enrollStartTime){
        this.enrollStartTime = DateUtils.getDateByString(enrollStartTime);
    }

    public void setEnrollEndTime(String enrollEndTime){
        this.enrollEndTime = DateUtils.getDateByString(enrollEndTime);
    }

    public void setContestStartTime(String contestStartTime){
        this.contestStartTime = DateUtils.getDateByString(contestStartTime);
    }

    public void setContestEndTime(String contestEndTime){
        this.contestEndTime = DateUtils.getDateByString(contestEndTime);
    }

    public ContestDetailEntity dto2Entity(){
        ContestDetailEntity contestDetailEntity = new ContestDetailEntity();
        contestDetailEntity.setEnrollStartTime(enrollStartTime);
        contestDetailEntity.setEnrollEndTime(enrollEndTime);
        contestDetailEntity.setContestStartTime(contestStartTime);
        contestDetailEntity.setContestEndTime(contestEndTime);
        contestDetailEntity.setContestId(contestId);
        contestDetailEntity.setContestSubject(contestSubject);
        contestDetailEntity.setContestPrice(contestPrice);
        contestDetailEntity.setRequiredContestPaying(requiredContestPaying);
        contestDetailEntity.setContestDescription(contestDescription);
        contestDetailEntity.setContestStatus(contestStatus);
        contestDetailEntity.setCreatedBy(createdBy);
        contestDetailEntity.setContestTypeId(contestTypeId);
        contestDetailEntity.setContestLevel(contestLevel);
        contestDetailEntity.setContestPicture(contestPicture);
        contestDetailEntity.setGroupingContest(groupingContest);
        contestDetailEntity.setGroupingMaxNum(groupingMaxNum);
        contestDetailEntity.setGroupingMinNum(groupingMinNum);
        contestDetailEntity.setAutoPrise(autoPrise);
        contestDetailEntity.setUsePercent(usePercent);
        return contestDetailEntity;
    }
}
