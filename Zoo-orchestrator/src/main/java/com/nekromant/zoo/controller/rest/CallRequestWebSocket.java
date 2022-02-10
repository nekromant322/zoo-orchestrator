package com.nekromant.zoo.controller.rest;

import com.nekromant.zoo.service.CallRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class CallRequestWebSocket extends TextWebSocketHandler {

    private WebSocketSession curSession;

    @Autowired
    private CallRequestService callRequestService;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        curSession = session;
        curSession.sendMessage(new TextMessage(String.valueOf(callRequestService.getRequestsCount())));
    }

    public void sendMessage(String str) {
        try {
            curSession.sendMessage(new TextMessage(str));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
