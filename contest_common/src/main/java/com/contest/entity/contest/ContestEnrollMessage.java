package com.contest.entity.contest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ContestEnrollMessage {
    private String userId;
    private Boolean pay;
    private Date createdDate;
    private String enrollId;
    private String contestSubject;
    private String contestId;
    private String typeName;
}
