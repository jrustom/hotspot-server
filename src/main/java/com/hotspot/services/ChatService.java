package com.hotspot.services;

import com.hotspot.UserPrincipal;
import com.hotspot.dto.ChatDtos.ChatResponseDto;
import com.hotspot.dto.MessageDtos.MessageRequestDto;
import com.hotspot.dto.MessageDtos.MessageResponseDto;
import com.hotspot.model.Chat;
import com.hotspot.model.Message;
import com.hotspot.repositories.ChatRepository;
import com.hotspot.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepo;
    private final AccountService accountService;
    private final MessageRepository messageRepo;

    public ChatResponseDto createChat() {
        Chat newChat = new Chat();
        return new ChatResponseDto(chatRepo.save(newChat));
    }

    public MessageResponseDto receiveMessage(String chatId,
                                              MessageRequestDto message,
                                              UserPrincipal sender) {
        LocalDateTime timeSent = LocalDateTime.now();

        Message newMessage = new Message(message.getContent(), timeSent,
                sender.getId(),
                chatId);

        messageRepo.save(newMessage);

        String senderUsername = sender.getName();

        return new MessageResponseDto(newMessage, senderUsername);
    }

    // Get messages relating to a specific chat
    public MessageResponseDto[] getSpecificMessages(String chatId, Integer messageCount) {
        List<Message> messages;
        if (messageCount != null) {
            messages = messageRepo.findByChatIdOrderByTimeSentDesc(chatId, PageRequest.of(0, messageCount));
        } else
            messages = messageRepo.findByChatId(chatId);

        return messages.stream().map((message) -> {
            String messageSender = getSenderUsername(message.getSenderId());
            return new MessageResponseDto(message, messageSender);
        }).toArray(MessageResponseDto[]::new);
    }

    private String getSenderUsername(String senderId) {
        return accountService.getAccount(senderId).getUsername();
    }
}
