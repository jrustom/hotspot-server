package com.hotspot.exceptions;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hotspot.dto.ErrorDto;

@ControllerAdvice
public class HotspotExceptionHandler {

    @ExceptionHandler(HotspotException.class)
    public ResponseEntity<ErrorDto> handleHotspotException(HotspotException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), e.getErrorCode().getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ArrayList<ErrorDto>> handleJakartaValidationException(MethodArgumentNotValidException e) {
        ArrayList<ErrorDto> errorMessages = new ArrayList<>();

        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errorMessages.add(new ErrorDto(error.getDefaultMessage()));
        }

        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }
}
