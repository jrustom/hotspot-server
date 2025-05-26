package com.hotspot.dto;

import lombok.Getter;

@Getter
public class ErrorDto {
    private final String message;
    private final Integer errorCode;

    public ErrorDto(String message, Integer errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
}