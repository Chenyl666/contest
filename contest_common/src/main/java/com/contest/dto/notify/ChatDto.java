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
public class ChatDto {
    private String sourceUserId;
    private String sourceUserName;
    private String targetUserId;
    private String targetUserName;
    private String msg;
    private Date createdDate;
}
