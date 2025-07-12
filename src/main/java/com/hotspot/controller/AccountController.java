package com.hotspot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
public class AccountController {
    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Get specific user
    @GetMapping("/{id}")
    public AccountResponseDto getUser(@PathVariable(name = "id") String id) {
        return accountService.getAccount(id);
    }

    @PostMapping("/login")
    public AccountResponseDto login(@RequestBody AccountLoginDto accountToLogin) {
        return accountService.login(accountToLogin);
    }

    @PostMapping("")
    public AccountResponseDto createUser(@Valid @RequestBody AccountCreationDto userToCreate) {
        return accountService.createAccount(userToCreate);
    }

    @PostMapping("/{id}")
    public void deleteAccount(@PathVariable(name = "id") String id) {
        accountService.deleteAccount(id);
    }
}
