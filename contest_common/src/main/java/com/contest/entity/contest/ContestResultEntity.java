package com.contest.entity.contest;

import com.contest.dto.contest.ContestResultDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ContestResultEntity {
    private Integer rank;
    private String userId;
    private Long contestId;
    private String contestSubject;
    private Float sumScore;

    public ContestResultDto entity2Dto(){
        return ContestResultDto
                .builder()
                .rank(rank)
                .userId(userId)
                .contestId(contestId.toString())
                .contestSubject(contestSubject)
                .sumScore(sumScore)
                .build();
    }
}
