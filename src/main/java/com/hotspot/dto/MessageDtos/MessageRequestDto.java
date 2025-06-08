package com.hotspot.dto.MessageDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MessageRequestDto {
    @NotBlank(message = "The message cannot be blank")
    private String content;
    private String senderId;
}
