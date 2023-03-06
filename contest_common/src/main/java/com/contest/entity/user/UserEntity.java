package com.contest.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.contest.dto.user.UserDetailDto;
import com.contest.dto.user.UserDto;
import com.contest.enu.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@TableName("user")
public class UserEntity {
    @TableId
    private String userId;
    @TableField("user_email")
    private String userEmail;
    @TableField("user_phone")
    private String userPhone;
    @TableField("resume_link")
    private String resumeLink;
    @TableField("user_name")
    private String userName;
    @TableField("identify")
    private String identify;
    @TableField("user_pic")
    private String userPic;
    @TableField("user_pwd")
    private String userPwd;
    @TableField("user_type")
    private UserType userType;
    @TableField("created_date")
    private Date createdDate;
    @TableField("updated_date")
    private Date updatedDate;
    @TableField("unit")
    private String unit;

    public void setUserType(String userType) {
        this.userType = UserType.valueOf(userType);
    }

    public UserDto entity2Dto(){
        return UserDto.builder()
                .userId(this.userId)
                .userEmail(this.userEmail)
                .userPhone(this.userPhone)
                .userName(this.userName)
                .userPic(this.userPic)
                .userType(this.userType)
                .build();
    }

    public UserDetailDto entity2DetailDto(){
        return UserDetailDto.builder()
                .userId(this.userId)
                .userEmail(this.userEmail)
                .userPhone(this.userPhone)
                .userName(this.userName)
                .userPic(this.userPic)
                .userType(this.userType)
                .unit(this.unit)
                .identify(this.identify)
                .resumeLink(this.resumeLink)
                .userPwd(this.userPwd)
                .build();
    }

}
