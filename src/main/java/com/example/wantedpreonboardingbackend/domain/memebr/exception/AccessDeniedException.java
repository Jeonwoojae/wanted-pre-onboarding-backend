package com.example.wantedpreonboardingbackend.domain.memebr.exception;

import com.example.wantedpreonboardingbackend.global.error.exception.BusinessException;
import com.example.wantedpreonboardingbackend.global.error.exception.ErrorCode;

public class AccessDeniedException extends BusinessException {
    public AccessDeniedException(String message) {
        super(message, ErrorCode.HANDLE_ACCESS_DENIED);
    }
}
