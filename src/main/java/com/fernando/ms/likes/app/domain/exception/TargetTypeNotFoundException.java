package com.fernando.ms.likes.app.domain.exception;

public class TargetTypeNotFoundException extends RuntimeException {
    public TargetTypeNotFoundException(String targetType) {
        super(targetType);
    }
}
