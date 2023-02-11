package com.contest.service.pic;

import com.contest.cnst.ServicesURL;
import com.contest.dto.user.UserDto;
import com.contest.entity.user.UserEntity;
import com.contest.mapper.UserMapper;
import com.contest.result.ResultModel;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

/**
 * 头像处理
 * */
@Service
public class PictureServiceImpl implements PictureService{

    @Resource
    private UserMapper userMapper;

    @Resource
    private RestTemplate restTemplate;

    public final String DEFAULT_PICTURE_URL = ServicesURL.CONTEST_FILESYS.concat(
            "/filesys/download/inline/422609470661595136"
    );

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
    @SneakyThrows
    @Override
    public void getPicture(String userId, OutputStream out) {
        Optional<UserEntity> userEntityOptional = Optional.ofNullable(userMapper.selectById(userId));
        if(userEntityOptional.isPresent()){
            String userPic = userEntityOptional.get().getUserPic();
            byte[] buf = null;
            try{
                buf = restTemplate.getForObject(
                        ServicesURL.CONTEST_FILESYS.concat("/filesys/download/inline/").concat(userPic),
                        byte[].class
                );
            }catch (Exception e){
                buf = restTemplate.getForObject(
                        DEFAULT_PICTURE_URL,
                        byte[].class
                );
            }finally {
                if(buf != null){
                    out.write(buf);
                    out.flush();
                }
            }
        }
    }


}
