package com.contest.websocket;

import com.alibaba.fastjson2.JSON;
import com.contest.dto.notify.ChatDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class WebSocketHandler {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @WebSocketRequestMapping("/chat")
    @SuppressWarnings("all")
    public void chat(WebsocketApi websocketApi, String dataJsonStr){
        ChatDto chatDto = JSON.parseObject(dataJsonStr, ChatDto.class);
        String targetUserId = chatDto.getTargetUserId();
        String sourceUserId = chatDto.getSourceUserId();
        Boolean isOnline = websocketApi.isOnline(targetUserId);
        String key1 = "CHAT::".concat(sourceUserId).concat("::").concat(targetUserId);
        String key2 = "CHAT::".concat(targetUserId).concat("::").concat(sourceUserId);
        Object objList1 = redisTemplate.opsForValue().get(key1);
        Object objList2 = redisTemplate.opsForValue().get(key2);
        List<ChatDto> chatList1;
        List<ChatDto> chatList2;
        if(objList1 == null){
            chatList1 = new ArrayList<>();
        }else{
            chatList1 = (List<ChatDto>) objList1;
        }
        if(objList2 == null){
            chatList2 = new ArrayList<>();
        }else{
            chatList2 = (List<ChatDto>) objList2;
        }
        chatList1.add(chatDto);
        chatList2.add(chatDto);
        redisTemplate.opsForValue().set(key1,chatList1);
        redisTemplate.opsForValue().set(key2,chatList2);
        if(isOnline){
            websocketApi.sendTo(targetUserId,WebSocketRequest.buildWebSocketRequest("/chat",chatDto));
        }
    }

}
