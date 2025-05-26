package com.hotspot.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hotspot.dto.ErrorDto;

@ControllerAdvice
public class HotspotExceptionHandler {

    @ExceptionHandler(HotspotException.class)
    public ResponseEntity<ErrorDto> handleHotspotException(HotspotException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage(), e.getErrorCode()), e.getStatus());
    }
}
