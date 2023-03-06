package com.contest.dto.contest;

import com.contest.entity.contest.ContestEnrollEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ContestEnrollDto {
    private String enrollId;
    private String contestId;
    private String userId;
    private Boolean pay;

    public ContestEnrollEntity dto2Entity(){
        return ContestEnrollEntity
                .builder()
                .enrollId(enrollId == null?null:Long.parseLong(enrollId))
                .contestId(contestId == null?null:Long.parseLong(contestId))
                .userId(userId)
                .pay(pay)
                .build();
    }

}

