package com.hotspot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotspot.dto.ChatDtos.ChatResponseDto;
import com.hotspot.model.Chat;
import com.hotspot.repositories.ChatRepository;

@Service
// This service is responsible for actions relating to chatting: sending
// messages, retrieving messages, deleting messages, opening a chat, etc
public class ChatService {
    private ChatRepository chatRepo;

    @Autowired
    public ChatService(ChatRepository chatRepo) {
        this.chatRepo = chatRepo;
    }

    public ChatResponseDto createChat() {
        Chat newChat = new Chat();

        return new ChatResponseDto(chatRepo.save(newChat));
    }




}
