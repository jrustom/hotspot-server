package com.hotspot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWSController {

    @Value("${WS_BROKER_URL}")
    private String ws_broker_url;

    @Value("${WS_BROKER_PREFIX}")
    private String ws_broker_prefix;

    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatWSController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/{chatId}/message/send")
    public void handleMessage(@DestinationVariable String chatId, String message) {
        messagingTemplate.convertAndSend(ws_broker_prefix + "/" + chatId + ws_broker_url, message);
    }

}