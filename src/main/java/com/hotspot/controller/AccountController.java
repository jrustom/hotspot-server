package com.hotspot.controller;

import com.hotspot.dto.AccountDtos.AccountCreationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotspot.dto.AccountDtos.AccountCreationDto;
import com.hotspot.dto.AccountDtos.AccountLoginDto;
import com.hotspot.dto.AccountDtos.AccountResponseDto;
import com.hotspot.services.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/login")
    public AccountCreationResponseDto login(@RequestBody AccountLoginDto accountToLogin) {
        return accountService.login(accountToLogin);
    }

    @PostMapping("/signup")
    public AccountCreationResponseDto createUser(@Valid @RequestBody AccountCreationDto userToCreate) {
        return accountService.createAccount(userToCreate);
    }

    @PostMapping("/{id}")
    public void deleteAccount(@PathVariable(name = "id") String id) {
        accountService.deleteAccount(id);
    }
}
