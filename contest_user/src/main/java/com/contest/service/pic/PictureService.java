package com.contest.service.pic;

import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;

import javax.servlet.http.HttpServletRequest;

public interface PictureService {
    public ResultModel<String> savePicture(UserDto userDto, String pictureUrl);

    public ResultModel<String> getPicture(UserDto userDto);
}
