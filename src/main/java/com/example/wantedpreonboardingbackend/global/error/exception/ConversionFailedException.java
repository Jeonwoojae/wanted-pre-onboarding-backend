package com.example.wantedpreonboardingbackend.global.error.exception;

public class ConversionFailedException extends BusinessException {
    public ConversionFailedException(String message) {
        super(message, ErrorCode.CONVERSION_FAILED);
    }
}
