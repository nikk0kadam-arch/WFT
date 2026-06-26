package com.ecommerce.shared.exception;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED", 401);
    }
}
