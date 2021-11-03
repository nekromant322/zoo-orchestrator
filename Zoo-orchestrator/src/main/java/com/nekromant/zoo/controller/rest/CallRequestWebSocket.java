package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.dao.CallRequestDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class CallRequestWebSocket extends TextWebSocketHandler {

    private WebSocketSession curSession;

    @Autowired
    private CallRequestDAO callRequestDAO;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        curSession = session;
        curSession.sendMessage(new TextMessage(String.valueOf(callRequestDAO.count())));
    }

    public void sendMessage(String str) {
        try {
            curSession.sendMessage(new TextMessage(str));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
