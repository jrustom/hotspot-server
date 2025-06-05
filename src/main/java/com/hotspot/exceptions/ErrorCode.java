package com.hotspot.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    USER_EMAIL_IN_USE(HttpStatus.BAD_REQUEST),
    USER_PW_INCORRECT(HttpStatus.BAD_REQUEST),

    // Hotspot
    HOTSPOT_NOT_FOUND(HttpStatus.NOT_FOUND),
    HOTSPOT_INVALID_VOTE(HttpStatus.BAD_REQUEST);

    private final HttpStatus status;

    ErrorCode(HttpStatus status) {
        this.status = status;
    }
}
