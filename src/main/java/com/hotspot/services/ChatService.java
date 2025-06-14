package com.hotspot.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.hotspot.dto.ChatDtos.ChatResponseDto;
import com.hotspot.dto.MessageDtos.MessageRequestDto;
import com.hotspot.dto.MessageDtos.MessageResponseDto;
import com.hotspot.model.Chat;
import com.hotspot.model.Message;
import com.hotspot.repositories.ChatRepository;
import com.hotspot.repositories.MessageRepository;

@Service
// This service is responsible for actions relating to chatting: sending
// messages, retrieving messages, deleting messages, opening a chat, etc
public class ChatService {
    private ChatRepository chatRepo;
    private MessageRepository messageRepo;

    @Autowired
    public ChatService(ChatRepository chatRepo, MessageRepository messageRepo) {
        this.chatRepo = chatRepo;
        this.messageRepo = messageRepo;
    }

    public ChatResponseDto createChat() {
        Chat newChat = new Chat();

        return new ChatResponseDto(chatRepo.save(newChat));
    }

    // This should save the message in the database and do whatever else it needs to
    // do
    public void receieveMessage(String chatId, MessageRequestDto message) {
        LocalDateTime timeSent = LocalDateTime.now();

        Message newMessage = new Message(message.getContent(), timeSent, message.getSenderId(), chatId);

        messageRepo.save(newMessage);
    }

    // Get messages relating to a specific chat
    public MessageResponseDto[] getSpecificMessages(String chatId, Integer messageCount) {
        List<Message> messages;
        if (messageCount != null) {
            messages = messageRepo.findByChatIdOrderByTimeSentDesc(chatId, PageRequest.of(0, messageCount));
        } else
            messages = messageRepo.findByChatId(chatId);

        return messages.stream().map((message) -> new MessageResponseDto(message)).toArray(MessageResponseDto[]::new);
    }
}
