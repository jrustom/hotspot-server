package com.hotspot.dto.AccountDtos;

import com.hotspot.model.User;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class AccountResponseDto {
    private final String id;
    private final String username;
    private final HashMap<String, User.VoteType> voteRecords;

    public AccountResponseDto(User person) {
        this.id = person.getId();
        this.username = person.getUsername();
        this.voteRecords = person.getVoteRecords();
    }
}
