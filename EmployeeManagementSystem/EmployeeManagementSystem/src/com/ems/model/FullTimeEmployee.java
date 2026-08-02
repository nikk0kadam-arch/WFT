package com.ems.model;

import com.ems.strategy.MonthlySalaryStrategy;
import com.ems.strategy.SalaryStrategy;

import java.time.LocalDate;
import java.util.Locale;

/**
 * A permanent, salaried employee. Salary = base monthly salary + bonus%.
 * Uses the MonthlySalaryStrategy (Strategy Pattern).
 */
public class FullTimeEmployee extends Employee {

    private double baseSalary;
    private double bonusPercent;
    private final SalaryStrategy salaryStrategy = new MonthlySalaryStrategy();

    public FullTimeEmployee(int id, String name, int age, String department,
                             LocalDate joiningDate, double baseSalary, double bonusPercent) {
        super(id, name, age, department, joiningDate);
        this.baseSalary = baseSalary;
        this.bonusPercent = bonusPercent;
    }

    // Overloaded constructor: no bonus specified -> defaults to 0%
    public FullTimeEmployee(int id, String name, int age, String department,
                             LocalDate joiningDate, double baseSalary) {
        this(id, name, age, department, joiningDate, baseSalary, 0.0);
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public double getBonusPercent() {
        return bonusPercent;
    }

    public void setBonusPercent(double bonusPercent) {
        this.bonusPercent = bonusPercent;
    }

    @Override
    public double calculateSalary() {
        return salaryStrategy.calculateSalary(this);
    }

    @Override
    public String getEmployeeType() {
        return "FULLTIME";
    }

    @Override
    public String toFileLine() {
        // type|id|name|age|department|joiningDate|baseSalary|bonusPercent
        return String.format(Locale.US, "FULLTIME|%d|%s|%d|%s|%s|%.2f|%.2f",
                id, name, age, department, joiningDate, baseSalary, bonusPercent);
    }
}
