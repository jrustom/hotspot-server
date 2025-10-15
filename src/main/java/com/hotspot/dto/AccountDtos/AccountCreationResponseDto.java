package com.hotspot.dto.AccountDtos;

import com.hotspot.dto.AccountDtos.AccountResponseDto;
import com.hotspot.model.User;
import lombok.Getter;

@Getter
public class AccountCreationResponseDto extends AccountResponseDto {
    private final String token;

    public AccountCreationResponseDto(User user, String token) {
        super(user);
        this.token = token;
    }
}
