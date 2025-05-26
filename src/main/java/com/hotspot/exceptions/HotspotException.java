package com.hotspot.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class HotspotException extends RuntimeException {
    private final HttpStatus status;
    private final Integer errorCode;

    public HotspotException(HttpStatus status, Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }
}
