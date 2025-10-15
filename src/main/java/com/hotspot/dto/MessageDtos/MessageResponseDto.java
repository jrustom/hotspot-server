package com.hotspot.dto.MessageDtos;

import java.time.LocalDateTime;

import com.hotspot.model.Message;

import lombok.Getter;

@Getter
public class MessageResponseDto {
    private String id;
    private String content;
    private LocalDateTime timeSent;
    private String senderUsername;

    public MessageResponseDto(Message message, String senderUsername) {
        this.id = message.getId();
        this.timeSent = message.getTimeSent();
        this.content = message.getContent();
        this.senderUsername = senderUsername;
    }
}
