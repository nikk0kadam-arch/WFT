package com.ems.strategy;

import com.ems.model.Employee;
import com.ems.model.InternEmployee;

/**
 * Salary calculation for interns: a fixed monthly stipend, no bonus
 * or hourly computation involved.
 */
public class StipendStrategy implements SalaryStrategy {

    @Override
    public double calculateSalary(Employee employee) {
        if (!(employee instanceof InternEmployee)) {
            throw new IllegalArgumentException(
                    "StipendStrategy can only be applied to InternEmployee instances");
        }
        InternEmployee intern = (InternEmployee) employee;
        return intern.getStipend();
    }
}
