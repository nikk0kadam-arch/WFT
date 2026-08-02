package com.ems.strategy;

import com.ems.model.Employee;

/**
 * Strategy Pattern: defines a common contract for salary calculation
 * algorithms. Concrete strategies (Monthly, Hourly, Stipend) plug into
 * any Employee subtype without changing the Employee class itself.
 */
public interface SalaryStrategy {
    double calculateSalary(Employee employee);
}
