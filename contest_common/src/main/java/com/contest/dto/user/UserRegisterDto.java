package com.contest.dto.user;

import com.contest.entity.user.UserEntity;
import com.contest.entity.user.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserRegisterDto implements Serializable {
    private String userId;
    private String userEmail;
    private String userPwd;
    private UserType userType;

    private String emailCode;

    public UserEntity toUserEntity(){
        return UserEntity.builder()
                .userId(this.userId)
                .userEmail(this.userEmail)
                .userPwd(this.userPwd)
                .userType(userType)
                .build();
    }

    public void setUserType(String userType) {
        this.userType = UserType.valueOf(userType);
    }
}
