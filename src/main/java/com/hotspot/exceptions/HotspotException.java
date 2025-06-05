package com.hotspot.exceptions;

import lombok.Getter;

@Getter
public class HotspotException extends RuntimeException {
    private final ErrorCode errorCode;

    public HotspotException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}