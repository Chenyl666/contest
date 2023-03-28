package com.contest.service.impl;

import com.contest.dto.notify.ChatDto;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;
import com.contest.service.ChatService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 获取聊天记录
     * */
    @Override
    public ResultModel<List<ChatDto>> getChatRecordsListBySourceUserId(String userId, UserDto userDto) {
        List<ChatDto> chatRecords = getChatRecords(userId, userDto.getUserId());
        if(chatRecords == null){
            chatRecords = new ArrayList<>();
        }
        return ResultModel.buildSuccessResultModel(null,chatRecords);
    }

    /**
     * 从缓存中获取聊天记录
     * */
    public List<ChatDto> getChatRecords(String userId,String selfId){
        String key = "CHAT::".concat(selfId).concat("::").concat(userId);
        Object records = redisTemplate.opsForValue().get(key);
        if(records == null){
            return null;
        }
        return (List<ChatDto>) records;
    }
}
