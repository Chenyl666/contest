package com.contest.websocket;

import com.alibaba.fastjson2.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket服务类
 * @author Chenyl
 * */
@SuppressWarnings("all")
@ServerEndpoint("/ws/{userId}")
@Component
@Slf4j
public class WebSocketPoint implements Serializable, WebsocketApi {

    /**
     * 静态变量，Websocket服务端
     * */

    //当前在线用户数量
    public static int onlineCount = 0;

    //存放每个在线用户的WebSocket对象，采用线程安全的ConcurrentHashMap来存储
    public static Map<String, WebSocketPoint> websocketMap = new ConcurrentHashMap<>();

    private static Map<String,Method> handlerMapping;

    private static WebSocketHandler webSocketHandler;

    static {
        Class<WebSocketHandler> clazz = WebSocketHandler.class;
        Method[] methods = clazz.getDeclaredMethods();
        handlerMapping = new HashMap<>();
        for (Method method : methods) {
            method.setAccessible(true);
            WebSocketRequestMapping annotation = method.getAnnotation(WebSocketRequestMapping.class);
            String webSocketRequestMappingValue = annotation.value();
            handlerMapping.put(webSocketRequestMappingValue,method);
        }
    }

    @Autowired
    private void setWebSocketHandler(WebSocketHandler webSocketHandler){
        this.webSocketHandler = webSocketHandler;
    }

    //用户连接时更新统计数据
    public synchronized void addOnlineCount(String userId,Session session){
        this.userId = userId;
        this.session = session;
        onlineCount++;
        WebSocketPoint webSocketPoint = getWebSocketPoint(this.userId);
        if(webSocketPoint != null){
            deleteWebSocketPoint(this.userId);
        }
        setWebsocketPoint(this.userId);
    }

    //用户断开时更新统计数据
    public synchronized void decreaseOnlineCount(String userId){
        onlineCount--;
        deleteWebSocketPoint(userId);
    }

    /**
     * 每个WebSocket对象特有的数据
     * */

    //用户名
    private String userId;

    //用户的连接会话
    private Session session;

    /**
     * 用户成功连接到WebSocket服务端时调用该方法
     * */
    @OnOpen
    public void open(Session session, @PathParam("userId")String userId){
        addOnlineCount(userId,session);
        log.info("用户" + userId + "已接入服务器!");
    }

    /**
     * 用户与WebSocket服务端断开连接时调用该方法
     * */
    @OnClose
    public void close(){
        decreaseOnlineCount(userId);
        log.info("用户" + userId + "已断开服务器!");
    }

    /**
     * WebSocket服务端接收到用户的信息时，调用该方法
     * */
    @SneakyThrows
    @OnMessage
    public void onMessage(String message,Session session){
        WebSocketRequest webSocketRequest = JSON.parseObject(message, WebSocketRequest.class);
        Method handler = handlerMapping.get(webSocketRequest.getRequest());
        System.out.println(handler.getName());
        handler.invoke(this.webSocketHandler,this,JSON.toJSONString(webSocketRequest.getData()));
    }

    /**
     * 出现错误时调用该方法
     * */
    @OnError
    public void onError(Session session,Throwable error){
        log.info("用户"+ userId +"出现错误");
        error.printStackTrace();
    }
    /**
     * 推送消息
     * */
    public void sendTo(String userId,WebSocketRequest webSocketRequest){
        WebSocketPoint webSocketPoint = null;
        //确认目标用户是否在线
        webSocketPoint = getWebSocketPoint(userId);
        if(webSocketPoint == null){
            return;
        }
        try {
            //开始向目标用户推送消息
            webSocketPoint.session.getBasicRemote().sendText(JSON.toJSONString(webSocketRequest));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Boolean isOnline(String userId) {
        WebSocketPoint websocketPoint = getWebSocketPoint(userId);
        return websocketPoint != null;
    }

    public WebSocketPoint getWebSocketPoint(String userId){
        return websocketMap.get(userId);
    }

    public void deleteWebSocketPoint(String userId){
        websocketMap.remove(userId);
    }

    public void setWebsocketPoint(String userId){
        websocketMap.put(userId,this);
    }
}


