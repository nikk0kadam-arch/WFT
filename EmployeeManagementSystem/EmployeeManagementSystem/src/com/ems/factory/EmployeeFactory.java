package com.ems.factory;

import com.ems.exception.InvalidEmployeeDataException;
import com.ems.model.ContractEmployee;
import com.ems.model.Employee;
import com.ems.model.FullTimeEmployee;
import com.ems.model.InternEmployee;

import java.time.LocalDate;

/**
 * Factory Method Pattern: centralizes creation logic for the different
 * Employee subtypes so client code never needs to call subtype
 * constructors directly.
 *
 * The meaning of amount1/amount2 depends on the type being created:
 *   FULLTIME -> amount1 = baseSalary,  amount2 = bonusPercent
 *   CONTRACT -> amount1 = hourlyRate,  amount2 = hoursWorked
 *   INTERN   -> amount1 = stipend,     amount2 = unused (pass 0)
 */
public class EmployeeFactory {

    // Private constructor: this is a static utility/factory, not meant to be instantiated.
    private EmployeeFactory() {
    }

    public static Employee createEmployee(String type, int id, String name, int age,
                                           String department, LocalDate joiningDate,
                                           double amount1, double amount2) {
        if (type == null || type.isBlank()) {
            throw new InvalidEmployeeDataException("Employee type must not be blank");
        }

        switch (type.trim().toUpperCase()) {
            case "FULLTIME":
                return new FullTimeEmployee(id, name, age, department, joiningDate, amount1, amount2);
            case "CONTRACT":
                return new ContractEmployee(id, name, age, department, joiningDate, amount1, amount2);
            case "INTERN":
                return new InternEmployee(id, name, age, department, joiningDate, amount1);
            default:
                throw new InvalidEmployeeDataException("Unknown employee type: " + type);
        }
    }
}
