package com.contest.service;

import com.contest.dto.notify.ChatDto;
import com.contest.dto.user.UserDto;
import com.contest.result.ResultModel;

import java.util.List;

public interface ChatService {
    /**
     * 获取聊天记录
     * */
    public ResultModel<List<ChatDto>> getChatRecordsListBySourceUserId(String userId, UserDto userDto);
}
