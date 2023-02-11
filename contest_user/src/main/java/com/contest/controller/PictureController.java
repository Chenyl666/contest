package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import com.contest.service.pic.PictureService;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user/pic")
public class PictureController {

    @Resource
    private PictureService pictureService;

    @PostMapping("/save")
    public ResultModel<String> savePicture(@CurrentUser UserDto userDto, @RequestBody String pictureUrl){
        return pictureService.savePicture(userDto, pictureUrl);
    }

    @SneakyThrows
    @GetMapping("/get/{user_id}")
    public void getPicture(@PathVariable("user_id") String userId, HttpServletResponse response){
        pictureService.getPicture(userId, response.getOutputStream());
    }
}
