package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import com.contest.service.pic.PictureService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user/pic")
public class PictureController {

    @Resource
    private PictureService pictureService;

    @PostMapping("/save")
    public ResultModel<String> savePicture(@CurrentUser UserDto userDto, String pictureUrl){
        return pictureService.savePicture(userDto, pictureUrl);
    }
}
