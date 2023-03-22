package com.contest.dto.notify;
import com.contest.entity.notify.NotifyContestEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NotifyContestSubmitDto {
    private Long contestId;
    private String title;
    private String content;

    public NotifyContestEntity dto2Entity(Long contestNotifyId, String createdBy){
        return NotifyContestEntity
                .builder()
                .contestNotifyId(contestNotifyId)
                .contestId(contestId)
                .title(title)
                .content(content)
                .createdBy(createdBy)
                .createdDate(new Date())
                .build();
    }
}
