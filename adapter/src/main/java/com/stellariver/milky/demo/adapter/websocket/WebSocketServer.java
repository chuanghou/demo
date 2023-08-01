package com.stellariver.milky.demo.adapter.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/ws")
public class WebSocketServer {


    @OnOpen
    public void onOpen(Session session) {
        String query = session.getRequestURI().getQuery();
        System.out.println(query);
    }

    @OnClose
    public void onClose(Session session) {

    }

    @OnMessage
    public String onMessage(String message, Session session) {
        System.out.println(message);
        return message;
    }


}
