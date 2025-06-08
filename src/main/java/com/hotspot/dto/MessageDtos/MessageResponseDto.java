package com.hotspot.dto.MessageDtos;

import java.time.LocalDateTime;

import com.hotspot.model.Message;

import lombok.Getter;

@Getter
public class MessageResponseDto {
    private String id;
    private String content;
    private LocalDateTime timeSent;
    private String senderId;

    public MessageResponseDto(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.timeSent = message.getTimeSent();
        this.senderId = message.getUserId();
    }
}
