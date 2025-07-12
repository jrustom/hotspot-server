package com.hotspot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotspot.dto.AccountDtos.AccountCreationDto;
import com.hotspot.dto.AccountDtos.AccountLoginDto;
import com.hotspot.dto.AccountDtos.AccountResponseDto;
import com.hotspot.dto.AccountDtos.AccountUpdateDto;
import com.hotspot.dto.AccountDtos.AccountUpdatePassDto;
import com.hotspot.exceptions.ErrorCode;
import com.hotspot.exceptions.HotspotException;
import com.hotspot.model.User;
import com.hotspot.repositories.UserRepository;

@Service
public class AccountService {
    private UserRepository userRepo;

    @Autowired
    public AccountService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User findUser(String id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new HotspotException(ErrorCode.USER_NOT_FOUND, "This user does not exist"));
    }

    public AccountResponseDto getAccount(String id) {
        return new AccountResponseDto(this.findUser(id));
    }

    public AccountResponseDto login(AccountLoginDto accountToLogin) {
        User userToLogin = userRepo.findByUsername(accountToLogin.getUsername()).orElseThrow(() -> new HotspotException(ErrorCode.USER_NOT_FOUND, "This user does not exist, create an account first."));

        if (!userToLogin.getPassword().equals(accountToLogin.getPassword())) {
            throw new HotspotException(ErrorCode.USER_PW_INCORRECT, "The password is incorrect");
        }

        return new AccountResponseDto(userToLogin);  
    }

    public AccountResponseDto createAccount(AccountCreationDto accountCreationInfo) {

        // Make sure email is not in use already
        if (userRepo.findByUsername(accountCreationInfo.getUsername()).isPresent()) {
            throw new HotspotException(ErrorCode.USER_EMAIL_IN_USE,
                    "A user with this username already exists, please try a different username");
        }

        // Initialize user to create
        User userToCreate = new User(accountCreationInfo.getUsername(), accountCreationInfo.getPassword(), accountCreationInfo.getProfilePicture());

        return new AccountResponseDto(userRepo.save(userToCreate));
    }

    // Updated fields: name (in future also language)
    public AccountResponseDto updateAccount(String id, AccountUpdateDto accountUpdateInfo) {
        // Find person to update
        User userToUpdate = this.findUser(id);
        // Update fields
        userToUpdate.setProfilePicture(accountUpdateInfo.getProfilePicture());

        // Persist updated person
        return new AccountResponseDto(userRepo.save(userToUpdate));
    }

    public AccountResponseDto updateAccountPassword(String id, AccountUpdatePassDto accountPassToUpdate) {
        // Find person to update
        User userToUpdate = this.findUser(id);

        // Validate password
        if (userToUpdate.getPassword() != accountPassToUpdate.getOldPass()) {
            throw new HotspotException(ErrorCode.USER_PW_INCORRECT, "The old password is incorrect");
        }

        userToUpdate.setPassword(accountPassToUpdate.getNewPass());

        return new AccountResponseDto(userRepo.save(userToUpdate));
    }

    public void deleteAccount(String id) {
        // Find person to delete
        User userToDelete = findUser(id);

        userRepo.delete(userToDelete);
    }
}
