package com.ems.strategy;

import com.ems.model.Employee;
import com.ems.model.FullTimeEmployee;

/**
 * Salary calculation for full-time employees: base salary plus a
 * percentage bonus on top.
 */
public class MonthlySalaryStrategy implements SalaryStrategy {

    @Override
    public double calculateSalary(Employee employee) {
        if (!(employee instanceof FullTimeEmployee)) {
            throw new IllegalArgumentException(
                    "MonthlySalaryStrategy can only be applied to FullTimeEmployee instances");
        }
        FullTimeEmployee fte = (FullTimeEmployee) employee;
        double bonusAmount = fte.getBaseSalary() * (fte.getBonusPercent() / 100.0);
        return fte.getBaseSalary() + bonusAmount;
    }
}
