package com.hotspot.services;

import com.hotspot.JwtService;
import com.hotspot.dto.AccountDtos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hotspot.exceptions.ErrorCode;
import com.hotspot.exceptions.HotspotException;
import com.hotspot.model.User;
import com.hotspot.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final JwtService jwtService;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public User findUser(String id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new HotspotException(ErrorCode.USER_NOT_FOUND, "This user does not exist"));
    }

    public AccountResponseDto getAccount(String id) {
        return new AccountResponseDto(this.findUser(id));
    }

    public AccountCreationResponseDto login(AccountLoginDto accountToLogin) {
        // Loads UserDetails by username then checks password - validating
        // credentials
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accountToLogin.getUsername(), accountToLogin.getPassword()));

        if (authentication.isAuthenticated()) {
            // Create JWT token
            String token =
                    jwtService.generateToken(((User) authentication.getPrincipal()).getUsername());

            // Can cast to User since our AccountDetailsService returns a User
            return new AccountCreationResponseDto((User) authentication.getPrincipal(), token);
        } else {
            throw new HotspotException(ErrorCode.USER_CREDENTIALS_INCORRECT, "The credentials are invalid.");
        }
    }

    public AccountCreationResponseDto createAccount(AccountCreationDto accountCreationInfo) {
        // Make sure email is not in use already
        if (userRepo.findByUsername(accountCreationInfo.getUsername()).isPresent()) {
            throw new HotspotException(ErrorCode.USER_EMAIL_IN_USE,
                    "A user with this username already exists, please try a different username");
        }

        // Initialize user to create
        User userToCreate = new User(accountCreationInfo.getUsername(), passwordEncoder.encode(accountCreationInfo.getPassword()), accountCreationInfo.getProfilePicture());

        // Create JWT token
        String token = jwtService.generateToken(accountCreationInfo.getUsername());

        return new AccountCreationResponseDto(userRepo.save(userToCreate), token);
    }

    public void deleteAccount(String id) {
        // Find person to delete
        User userToDelete = findUser(id);

        userRepo.delete(userToDelete);
    }
}
