package com.hotspot.controller;

import com.hotspot.dto.MessageDtos.MessageRequestDto;
import com.hotspot.dto.MessageDtos.MessageResponseDto;
import com.hotspot.services.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWSController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${WS_BROKER_URL}")
    private String ws_broker_url;

    @Value("${WS_BROKER_PREFIX}")
    private String ws_broker_prefix;

    @MessageMapping("/{chatId}/message/send")
    public void handleMessage(@DestinationVariable String chatId, @Valid MessageRequestDto message) {
        MessageResponseDto response = chatService.receieveMessage(chatId, message);
        messagingTemplate.convertAndSend(ws_broker_prefix + "/" + chatId + ws_broker_url, response);
    }

}