package com.hotspot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.hotspot.dto.MessageDtos.MessageRequestDto;
import com.hotspot.services.ChatService;

import jakarta.validation.Valid;

@Controller
public class ChatWSController {
    private ChatService chatService; 

    @Value("${WS_BROKER_URL}")
    private String ws_broker_url;

    @Value("${WS_BROKER_PREFIX}")
    private String ws_broker_prefix;

    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatWSController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/{chatId}/message/send")
    public void handleMessage(@DestinationVariable String chatId, @Valid MessageRequestDto message) {
        chatService.receieveMessage(chatId, message);
        messagingTemplate.convertAndSend(ws_broker_prefix + "/" + chatId + ws_broker_url, message);
    }

}