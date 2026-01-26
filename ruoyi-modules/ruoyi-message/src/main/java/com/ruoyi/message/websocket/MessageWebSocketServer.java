package com.ruoyi.message.websocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.message.domain.SysUserMessage;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket消息服务端
 * 接口路径：ws://localhost:9204/ws/message/{userId}
 * 注意：微服务下端口可能不同，需确认 ruoyi-message 的端口
 */

@Component
@ServerEndpoint("/ws/message/{userId}")
public class MessageWebSocketServer {
    private static final Logger log = LoggerFactory.getLogger(MessageWebSocketServer.class);

    // 存储在线用户 Session (key: userId, value: Session集合)
    private static final ConcurrentHashMap<Long, CopyOnWriteArraySet<Session>> webSocketMap = new ConcurrentHashMap<>();

    private Long userId;
    private Session session;

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        this.session = session;
        this.userId = userId;

        webSocketMap.computeIfAbsent(userId, k -> new CopyOnWriteArraySet<>()).add(session);
        log.info("【WebSocket】用户 {} 连接成功，当前在线人数: {}", userId, webSocketMap.size());
    }

    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.get(userId).remove(session);
            if (webSocketMap.get(userId).isEmpty()) {
                webSocketMap.remove(userId);
            }
        }
        log.info("【WebSocket】用户 {} 断开连接", userId);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("【WebSocket】连接异常", error);
    }

    /**
     * 核心功能：发送消息给指定用户
     */
    public static void sendToUser(Long userId, String messageJson) {
        if (webSocketMap.containsKey(userId)) {
            for (Session session : webSocketMap.get(userId)) {
                try {
                    session.getBasicRemote().sendText(messageJson);
                } catch (IOException e) {
                    log.error("【WebSocket】发送消息失败: {}", e.getMessage());
                }
            }
        } else {
            log.info("【WebSocket】用户 {} 不在线，消息仅存库", userId);
        }
    }
}