package com.stellariver.milky.demo.adapter.websocket;

import com.stellariver.milky.common.tool.common.Kit;
import com.stellariver.milky.demo.basic.TokenUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/ws")
public class WsHandler {

    static private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        String token = session.getRequestParameterMap().get("token").get(0);
        String userId = TokenUtils.getUserId(token);
        sessions.put(userId, session);
    }

    @OnClose
    public void onClose(Session session) {
        String userId = sessions.entrySet().stream()
                .filter(e -> Kit.eq(e.getValue().getId(), session.getId()))
                .map(Map.Entry::getKey).findFirst().orElse(null);
        if (StringUtils.isNotBlank(userId)) {
            sessions.remove(userId);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        String userId = sessions.entrySet().stream()
                .filter(e -> Kit.eq(e.getValue().getId(), session.getId()))
                .map(Map.Entry::getKey).findFirst().orElse(null);
        log.error("userId {} ", userId, error);
        sessions.remove(userId);
    }

    @SneakyThrows
    static public void push(String userId, String message) {
        Session session = sessions.get(userId);
        if (session == null) {
            return;
        }
        session.getBasicRemote().sendText(message);
    }

}
