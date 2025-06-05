package com.hotspot.dto;

import lombok.Getter;

@Getter
public class ErrorDto {
    private final String message;

    public ErrorDto(String message) {
        this.message = message;
    }
}