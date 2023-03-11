package com.contest.entity.notify;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@TableName("notify_message")
public class NotifyMessageEntity {
    @TableField("message_id")
    private Long messageId;
    @TableField("message_title")
    private String messageTitle;
    @TableField("message_content")
    private String messageContent;
    @TableField("created_by")
    private String createdBy;
    @TableField("has_read")
    private Boolean hasRead;
    @TableField("receiver")
    private String receiver;
    @TableField("created_date")
    private Date createdDate;
}
