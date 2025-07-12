package com.hotspot.dto.AccountDtos;

import com.hotspot.model.User;

import lombok.Getter;

@Getter
public class AccountResponseDto {
    private final String id;
    private final String username;

    public AccountResponseDto(User person) {
        this.id = person.getId();
        this.username = person.getUsername();
    }
}
