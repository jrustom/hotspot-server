package com.hotspot.dto.AccountDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountUpdateDto {
    @NotBlank(message = "The new name must not be blank")
    private final String profilePicture;
}
