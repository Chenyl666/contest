package com.contest.dto.notify;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NotifyEmailDto {
    private String messageTitle;
    private String messageContent;
    private String createdBy;
    private String receiver;
}
