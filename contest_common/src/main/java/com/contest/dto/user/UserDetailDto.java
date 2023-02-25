package com.contest.dto.user;

import com.contest.entity.user.UnitEntity;
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
    private UnitEntity unit;

    public void setUserType(String officeCode) {
        this.userType = UserType.valueOf(officeCode);
    }

}
