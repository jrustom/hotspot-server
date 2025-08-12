package com.hotspot.dto.MessageDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequestDto {
    @NotBlank(message = "The message cannot be blank")
    private String content;
    private String senderId;
}
