package com.contest.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class WebSocketRequest<T> {
    private String request;
    private T data;

    public static <T> WebSocketRequest<T> buildWebSocketRequest(String request, T data){
        return new WebSocketRequest<T>(request,data);
    }
}
