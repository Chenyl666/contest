package com.contest.dto.contest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ContestUserResult {
    private String userId;
    private String userName;
    private String unit;
    private String contestSubject;
    private String contestId;
    private String identify;
    private Float sumScore;
    private Integer rank;
}
