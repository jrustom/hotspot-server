package com.hotspot.dto.ChatDtos;

import com.hotspot.model.Chat;

import lombok.Getter;

@Getter
public class ChatResponseDto {
    private final String id;

    public ChatResponseDto(Chat chat) {
        this.id = chat.getId();
    }
}
