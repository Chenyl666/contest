package com.contest.dto.notify;

import com.contest.entity.notify.NotifyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NotifyDto {
    private String messageId;
    private String messageTitle;
    private String messageContent;
    private String creatorName;
    private Boolean hasRead;
    private String receiver;
    private Date createdDate;

    public NotifyEntity dto2Entity(){
        return NotifyEntity
                .builder()
                .messageId(messageId == null?null:Long.parseLong(messageId))
                .messageTitle(messageTitle)
                .messageContent(messageContent)
                .creatorName(creatorName)
                .hasRead(hasRead)
                .receiver(receiver)
                .createdDate(createdDate)
                .build();
    }
}
