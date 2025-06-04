package com.hotspot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.hotspot.dto.AccountDtos.AccountCreationDto;
import com.hotspot.dto.AccountDtos.AccountResponseDto;
import com.hotspot.dto.AccountDtos.AccountUpdateDto;
import com.hotspot.dto.AccountDtos.AccountUpdatePassDto;
import com.hotspot.exceptions.HotspotException;
import com.hotspot.model.User;
import com.hotspot.repositories.UserRepository;

@Service
// This service is responsible for actions relating to accounts, including
// signing in, signging up, deleting account, updating account info,
public class AccountService {
    private UserRepository userRepo;

    @Autowired
    public AccountService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User findUser(String id, Integer errorCode) {
        return userRepo.findById(id)
                .orElseThrow(() -> new HotspotException(HttpStatus.NOT_FOUND, errorCode, "This user does not exist"));
    }

    public AccountResponseDto getAccount(String id) {
        return new AccountResponseDto(this.findUser(id, 100));
    }

    public AccountResponseDto createAccount(AccountCreationDto accountCreationInfo) {

        // Make sure email is not in use already
        if (userRepo.findByEmail(accountCreationInfo.getEmail()).isPresent()) {
            throw new HotspotException(HttpStatus.BAD_REQUEST, 100,
                    "A user with this email already exists, please try a different email");
        }

        // Initialize user to create
        User userToCreate = new User(accountCreationInfo.getName(), accountCreationInfo.getEmail(),
                accountCreationInfo.getPassword());

        return new AccountResponseDto(userRepo.save(userToCreate));
    }

    // Updated fields: name (in future also language)
    public AccountResponseDto updateAccount(String id, AccountUpdateDto accountUpdateInfo) {
        // Find person to update
        User userToUpdate = this.findUser(id, 100);
        // Update fields
        userToUpdate.setName(accountUpdateInfo.getName());

        // Persist updated person
        return new AccountResponseDto(userRepo.save(userToUpdate));
    }

    public AccountResponseDto updateAccountPassword(String id, AccountUpdatePassDto accountPassToUpdate) {
        // Find person to update
        User userToUpdate = this.findUser(id, 100);

        // Validate password
        if (userToUpdate.getPassword() != accountPassToUpdate.getOldPass()) {
            throw new HotspotException(HttpStatus.BAD_REQUEST, 100, "The old password is incorrect");
        }

        userToUpdate.setPassword(accountPassToUpdate.getNewPass());

        return new AccountResponseDto(userRepo.save(userToUpdate));
    }

    public void deleteAccount(String id) {
        // Find person to delete
        User userToDelete = findUser(id, 100);

        userRepo.delete(userToDelete);
    }
}
