package com.contest.websocket;

public interface WebsocketApi {
    public Boolean isOnline(String userId);

    public void sendTo(String userId,WebSocketRequest webSocketRequest);
}
