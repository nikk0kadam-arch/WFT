package com.ems.service;

/**
 * Lightweight, read-only projection of an Employee used for reports and
 * external-facing views (demonstrates Stream.map() to a DTO shape).
 */
public class EmployeeDTO {

    private final int id;
    private final String name;
    private final String department;
    private final double salary;

    public EmployeeDTO(int id, String name, String department, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return String.format("EmployeeDTO{id=%d, name='%s', department='%s', salary=%.2f}",
                id, name, department, salary);
    }
}
