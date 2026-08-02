package com.ems.strategy;

import com.ems.model.ContractEmployee;
import com.ems.model.Employee;

/**
 * Salary calculation for contract employees: hourly rate multiplied
 * by the number of hours worked in the period.
 */
public class HourlyWageStrategy implements SalaryStrategy {

    @Override
    public double calculateSalary(Employee employee) {
        if (!(employee instanceof ContractEmployee)) {
            throw new IllegalArgumentException(
                    "HourlyWageStrategy can only be applied to ContractEmployee instances");
        }
        ContractEmployee contractor = (ContractEmployee) employee;
        return contractor.getHourlyRate() * contractor.getHoursWorked();
    }
}
