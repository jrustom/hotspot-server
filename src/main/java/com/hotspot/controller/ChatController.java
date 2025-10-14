package com.hotspot.controller;

import com.hotspot.dto.MessageDtos.MessageResponseDto;
import com.hotspot.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chats/")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    // Get messages for a chat
    @GetMapping("{cid}")
    public MessageResponseDto[] getMessages(@PathVariable(name = "cid") String chatId,
                                            @RequestParam(name = "count", required = false) Integer messageCount) {
        return chatService.getSpecificMessages(chatId, messageCount);
    }

}
