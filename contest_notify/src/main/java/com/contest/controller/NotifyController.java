package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.notify.NotifyContestDto;
import com.contest.dto.notify.NotifyContestSubmitDto;
import com.contest.dto.notify.NotifyDto;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import com.contest.service.NotifyContestService;
import com.contest.service.NotifyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/contest/notify")
public class NotifyController {

    @Resource
    private NotifyService notifyService;

    @Resource
    private NotifyContestService notifyContestService;

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

    /**
     * 发布竞赛通知
     * */
    @PostMapping("/publish")
    public ResultModel<String> publishNotify(
            @RequestBody NotifyContestSubmitDto notifyContestSubmitDto,
            @CurrentUser UserDto userDto){
        return notifyContestService.publishNotifyContest(notifyContestSubmitDto, userDto);
    }

    /**
     * 加载竞赛通知
     * */
    @GetMapping("/get/publish/{contest_id}")
    public ResultModel<List<NotifyContestDto>> getNotifyContestListByContestId(
            @PathVariable("contest_id")Long contestId,
            @CurrentUser UserDto userDto){
        return notifyContestService.getNotifyContestListByContestId(contestId,userDto);
    }

    /**
     * 删除竞赛通知
     * */
    @DeleteMapping("/delete/publish/{contest_notify_id}")
    public ResultModel<String> deletePublishByContestNotifyId(@PathVariable("contest_notify_id")Long contestNotifyId){
        return notifyContestService.deletePublishByContestNotifyId(contestNotifyId);
    }
}
