package com.example.coreservice.exception;

public class CustomBusinessException extends RuntimeException {
    public CustomBusinessException(String message) {
        super(message);
    }
}
