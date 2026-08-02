package com.ems.exception;

/**
 * Custom checked-like runtime exception thrown when an employee lookup
 * (by id, or otherwise) fails to find a match in the repository.
 */
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(int id) {
        super("Employee not found with id: " + id);
    }

    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
