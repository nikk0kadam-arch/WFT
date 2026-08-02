package com.ems.exception;

/**
 * Thrown when a requested Employee (or other resource) does not exist.
 * Translated to HTTP 404 by GlobalExceptionHandler.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException forEmployee(Long id) {
        return new ResourceNotFoundException("Employee not found with id: " + id);
    }
}
