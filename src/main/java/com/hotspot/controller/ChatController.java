package com.hotspot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotspot.dto.MessageDtos.MessageResponseDto;
import com.hotspot.services.ChatService;

@RestController
@RequestMapping("/chats/")
public class ChatController {
    private ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // Get all messages for a chat
    @GetMapping("{cid}")
    public MessageResponseDto[] getSpecificMessages(@PathVariable(name = "cid") String chatId) {
        return chatService.getSpecificMessages(chatId, null);
    }

    // Get a specific number of messages for a chat
    @GetMapping("{cid}")
    public MessageResponseDto[] getSpecificNumberOfMessages(@PathVariable(name = "cid") String chatId,
            @RequestParam(name = "count") Integer messageCount) {
        return chatService.getSpecificMessages(chatId, messageCount);
    }

}
