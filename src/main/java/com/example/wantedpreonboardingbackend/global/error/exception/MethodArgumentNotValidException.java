package com.example.wantedpreonboardingbackend.global.error.exception;

public class MethodArgumentNotValidException extends BusinessException{
    public MethodArgumentNotValidException(String message) {
        super(message, ErrorCode.LOGIN_INPUT_INVALID);
    }
}
