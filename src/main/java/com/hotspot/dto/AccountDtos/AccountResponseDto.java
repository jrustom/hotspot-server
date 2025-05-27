package com.hotspot.dto.AccountDtos;

import com.hotspot.model.User;

import lombok.Getter;

@Getter
public class AccountResponseDto {
    private final Integer id;
    private final String name;
    private final String email;

    public AccountResponseDto(User person) {
        this.id = person.getId();
        this.name = person.getName();
        this.email = person.getEmail();
    }
}
