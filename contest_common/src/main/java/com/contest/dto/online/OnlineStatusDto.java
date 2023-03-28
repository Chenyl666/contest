package com.contest.dto.online;

import com.contest.dto.user.UserDto;
import com.contest.entity.contest.ContestEnrollEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OnlineStatusDto {
    private String contestId;
    private String userId;
    private String userName;
    private ContestEnrollEntity.Status status;

    public static OnlineStatusDto buildOnlineStatusDto(ContestEnrollEntity contestEnrollEntity, UserDto userDto){
        if(!contestEnrollEntity.getUserId().equals(userDto.getUserId())){
            throw new IllegalArgumentException();
        }
        return OnlineStatusDto
                .builder()
                .contestId(contestEnrollEntity.getContestId().toString())
                .userId(contestEnrollEntity.getUserId())
                .userName(userDto.getUserName())
                .status(contestEnrollEntity.getStatus())
                .build();
    }
}
