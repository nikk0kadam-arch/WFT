package com.ems.model;

import com.ems.strategy.HourlyWageStrategy;
import com.ems.strategy.SalaryStrategy;

import java.time.LocalDate;
import java.util.Locale;

/**
 * A contractor paid per hour. Salary = hourlyRate * hoursWorked.
 * Uses the HourlyWageStrategy (Strategy Pattern).
 */
public class ContractEmployee extends Employee {

    private double hourlyRate;
    private double hoursWorked;
    private final SalaryStrategy salaryStrategy = new HourlyWageStrategy();

    public ContractEmployee(int id, String name, int age, String department,
                             LocalDate joiningDate, double hourlyRate, double hoursWorked) {
        super(id, name, age, department, joiningDate);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    @Override
    public double calculateSalary() {
        return salaryStrategy.calculateSalary(this);
    }

    @Override
    public String getEmployeeType() {
        return "CONTRACT";
    }

    @Override
    public String toFileLine() {
        // type|id|name|age|department|joiningDate|hourlyRate|hoursWorked
        return String.format(Locale.US, "CONTRACT|%d|%s|%d|%s|%s|%.2f|%.2f",
                id, name, age, department, joiningDate, hourlyRate, hoursWorked);
    }
}
