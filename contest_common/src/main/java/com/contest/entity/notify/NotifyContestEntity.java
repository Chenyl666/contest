package com.contest.entity.notify;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@TableName("notify_contest")
public class NotifyContestEntity {
    @TableId
    private Long contestNotifyId;
    @TableField("contest_id")
    private Long contestId;
    @TableField("title")
    private String title;
    @TableField("content")
    private String content;
    @TableField("created_by")
    private String createdBy;
    @TableField("created_date")
    private Date createdDate;
}
