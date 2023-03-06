package com.contest.dto.user;

import com.contest.entity.user.UserEntity;
import com.contest.enu.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDetailDto {
    private String userId;
    private String userEmail;
    private String userPhone;
    private String resumeLink;
    private String userName;
    private String identify;
    private String userPic;
    private String userPwd;
    private UserType userType;
    private String unit;

    public void setUserType(String officeCode) {
        this.userType = UserType.valueOf(officeCode);
    }

    public UserEntity detailDto2Entity(){
        return UserEntity
                .builder()
                .userId(userId)
                .userEmail(userEmail)
                .userPwd(userPwd)
                .userPhone(userPhone)
                .resumeLink(resumeLink)
                .userName(userName)
                .identify(identify)
                .userPic(userPic)
                .userType(userType)
                .unit(unit)
                .build();
    }

}
