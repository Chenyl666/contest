package com.contest.dto.notify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NotifyContestDto {
    private String contestNotifyId;
    private String title;
    private String contestSubject;
    private String createdBy;
    private Date createdDate;
    private String content;
}
