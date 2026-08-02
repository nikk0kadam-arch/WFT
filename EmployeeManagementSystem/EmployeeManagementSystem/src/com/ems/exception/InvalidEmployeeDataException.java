package com.ems.exception;

/**
 * Thrown when employee data fails validation (e.g. negative salary,
 * blank name, invalid age, malformed file record).
 */
public class InvalidEmployeeDataException extends RuntimeException {

    public InvalidEmployeeDataException(String message) {
        super(message);
    }

    public InvalidEmployeeDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
