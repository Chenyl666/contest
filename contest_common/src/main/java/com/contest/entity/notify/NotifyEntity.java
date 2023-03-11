package com.contest.entity.notify;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.contest.dto.notify.NotifyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@TableName("notify")
public class NotifyEntity {
    @TableField("message_id")
    private Long messageId;
    @TableField("message_title")
    private String messageTitle;
    @TableField("message_content")
    private String messageContent;
    @TableField("creator_name")
    private String creatorName;
    @TableField("has_read")
    private Boolean hasRead;
    @TableField("receiver")
    private String receiver;
    @TableField("created_date")
    private Date createdDate;

    public NotifyDto entity2Dto(){
        return NotifyDto
                .builder()
                .messageId(String.valueOf(messageId))
                .messageTitle(messageTitle)
                .messageContent(messageContent)
                .creatorName(creatorName)
                .hasRead(hasRead)
                .receiver(receiver)
                .createdDate(createdDate)
                .build();
    }
}
