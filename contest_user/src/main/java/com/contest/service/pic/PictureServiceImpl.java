package com.contest.service.pic;

import com.contest.dto.user.UserDto;
import com.contest.mapper.UserMapper;
import com.contest.result.ResultModel;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 头像处理
 * */
@Service
public class PictureServiceImpl implements PictureService{

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private UserMapper userMapper;

    /**
     * 修改头像
     * */
    @Override
    public ResultModel<String> savePicture(UserDto userDto, String pictureUrl) {
        userMapper.updateUserPicByUserId(pictureUrl, userDto.getUserId());
        return ResultModel.buildSuccessResultModel();
    }

    /**
     * 获取用户头像链接
     * */
    @Override
    public ResultModel<String> getPicture(UserDto userDto) {
        return ResultModel.buildSuccessResultModel(userMapper.selectById(userDto.getUserId()).getUserPic());
    }


}
