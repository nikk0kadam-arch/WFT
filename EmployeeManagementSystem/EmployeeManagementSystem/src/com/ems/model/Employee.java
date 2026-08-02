package com.ems.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * Abstract base class representing a generic Employee.
 * Demonstrates: fields, encapsulation, constructor overloading,
 * Java 8 Date/Time API (LocalDate/Period), and OOP fundamentals.
 */
public abstract class Employee {

    protected int id;
    protected String name;
    protected int age;
    protected String department;
    protected LocalDate joiningDate;

    protected Employee() {
    }

    // Full constructor
    public Employee(int id, String name, int age, String department, LocalDate joiningDate) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.department = department;
        this.joiningDate = joiningDate;
    }

    // Overloaded constructor: defaults department to "General" when not supplied
    public Employee(int id, String name, int age, LocalDate joiningDate) {
        this(id, name, age, "General", joiningDate);
    }

    // ---- Getters & Setters (Encapsulation) ----

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    /**
     * Java 8 Date/Time API: calculates full years of experience using Period.
     */
    public int getExperienceInYears() {
        return Period.between(joiningDate, LocalDate.now()).getYears();
    }

    /**
     * Every concrete employee type must be able to compute its own salary
     * (delegated internally to a SalaryStrategy implementation).
     */
    public abstract double calculateSalary();

    /**
     * Every concrete employee type must identify its type for reporting/serialization.
     */
    public abstract String getEmployeeType();

    /**
     * Serializes this employee to a pipe-delimited line for file storage.
     * Subclasses append their own type-specific fields.
     */
    public abstract String toFileLine();

    @Override
    public String toString() {
        return String.format(
                "[%-4d] %-18s | Age:%-3d | Dept:%-12s | Type:%-10s | Joined:%-10s | Exp:%2dy | Salary: %10.2f",
                id, name, age, department, getEmployeeType(), joiningDate,
                getExperienceInYears(), calculateSalary());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
