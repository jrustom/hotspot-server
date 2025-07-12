package com.hotspot.dto.AccountDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountLoginDto {
    @NotBlank(message = "The username must not be blank")
    private final String username;
    @NotBlank(message = "The password must not be blank")
    private final String password;
}
