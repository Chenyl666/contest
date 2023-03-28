package com.contest.controller;

import com.contest.annotation.currentuser.CurrentUser;
import com.contest.dto.notify.ChatDto;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import com.contest.service.ChatService;
import com.contest.websocket.WebSocketPoint;
import com.contest.websocket.WebSocketRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/contest/chat")
public class ChatController {

    @Resource
    private ChatService chatService;

    /**
     * 获取聊天记录
     * */
    @GetMapping("/records/{userId}")
    public ResultModel<List<ChatDto>> getChatRecordsListByUserId(
            @PathVariable("userId")String userId,
            @CurrentUser UserDto userDto
    ){
        return chatService.getChatRecordsListBySourceUserId(userId,userDto);
    }

    @PostMapping("/try")
    public void send(){
        WebSocketPoint websocketPoint = WebSocketPoint.websocketMap.get("dummy666");
        websocketPoint.sendTo("dummy666", WebSocketRequest
                .<ChatDto>builder()
                        .request("/chat")
                        .data(ChatDto.builder().sourceUserId("chenyl666").sourceUserName("考官").createdDate(new Date()).msg("测试消息!!!!").targetUserId("dummy666").targetUserName("陈奕霖").build())
                .build());
    }

}
