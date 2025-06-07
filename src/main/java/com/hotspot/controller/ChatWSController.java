package com.hotspot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWSController {

    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatWSController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/{chatId}/message/send")
    public void handleMessage(@DestinationVariable String chatId, String message) {
        messagingTemplate.convertAndSend("/chat/" + chatId + "/messages", message);
    }

}