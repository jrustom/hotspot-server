package com.hotspot.dto.AccountDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountCreationDto {
    @NotBlank(message = "The name must not be blank")
    private final String name;
    @NotBlank(message = "The email must not be blank")
    @Email(message = "The email is invalid")
    private final String email;
    @NotBlank(message = "The password must not be blank")
    private final String password;
}
