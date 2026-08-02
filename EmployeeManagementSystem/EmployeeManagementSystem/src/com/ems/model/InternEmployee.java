package com.ems.model;

import com.ems.strategy.SalaryStrategy;
import com.ems.strategy.StipendStrategy;

import java.time.LocalDate;
import java.util.Locale;

/**
 * An intern paid a fixed monthly stipend.
 * Uses the StipendStrategy (Strategy Pattern).
 */
public class InternEmployee extends Employee {

    private double stipend;
    private final SalaryStrategy salaryStrategy = new StipendStrategy();

    public InternEmployee(int id, String name, int age, String department,
                           LocalDate joiningDate, double stipend) {
        super(id, name, age, department, joiningDate);
        this.stipend = stipend;
    }

    public double getStipend() {
        return stipend;
    }

    public void setStipend(double stipend) {
        this.stipend = stipend;
    }

    @Override
    public double calculateSalary() {
        return salaryStrategy.calculateSalary(this);
    }

    @Override
    public String getEmployeeType() {
        return "INTERN";
    }

    @Override
    public String toFileLine() {
        // type|id|name|age|department|joiningDate|stipend|0 (unused second slot, kept for uniform column count)
        return String.format(Locale.US, "INTERN|%d|%s|%d|%s|%s|%.2f|%.2f",
                id, name, age, department, joiningDate, stipend, 0.0);
    }
}
