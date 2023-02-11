package com.contest.service.pic;

import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;

import javax.servlet.http.HttpServletRequest;
import java.io.OutputStream;

public interface PictureService {
    public ResultModel<String> savePicture(UserDto userDto, String pictureUrl);

    public void getPicture(String userId, OutputStream out);
}
