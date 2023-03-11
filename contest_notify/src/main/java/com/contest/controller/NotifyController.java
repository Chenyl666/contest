package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.notify.NotifyDto;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import com.contest.service.NotifyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/contest/notify")
public class NotifyController {

    @Resource
    private NotifyService notifyService;

    /**
     * 获取通知页
     * */
    @GetMapping("/page/{page}")
    public ResultModel<List<NotifyDto>> getNotifyDtoPage(
            @PathVariable("page") Integer page,
            @CurrentUser UserDto userDto
    ){
        return notifyService.getNotifyList(userDto,page);
    }

    /**
     * 标记消息已读
     * */
    @PostMapping("/read")
    public void setHasRead(@RequestParam("messageId")Long messageId){
        notifyService.setHasRead(messageId);
    }

    /**
     * 获取未读消息数量
     * */
    @GetMapping("/unread/count")
    public ResultModel<Integer> getUnreadNotifyMessageCount(@CurrentUser UserDto userDto){
        return notifyService.getUnreadNotifyMessageCount(userDto);
    }

    /**
     * 通过id删除消息
     * */
    @DeleteMapping("/delete/{message_id}")
    public ResultModel<String> deleteById(@PathVariable("message_id")Long messageId){
        return notifyService.deleteById(messageId);
    }

}
