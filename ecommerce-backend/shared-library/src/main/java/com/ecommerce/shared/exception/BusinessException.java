package com.ecommerce.shared.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String errorCode;
    private final int statusCode;

    public BusinessException(String message, String errorCode, int statusCode) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public BusinessException(String message, Throwable cause, String errorCode, int statusCode) {
        super(message, cause);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }
}
