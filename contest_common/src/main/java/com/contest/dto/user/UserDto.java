package com.contest.dto.user;

import com.contest.entity.user.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
    private String userId;
    private String userEmail;
    private String userPhone;
    private String userName;
    private String userPic;
    private UserType userType;
}
