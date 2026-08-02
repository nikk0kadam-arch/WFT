package com.ems.config;

import com.ems.model.Employee;
import com.ems.model.EmployeeType;
import com.ems.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Seeds a handful of sample employees on startup so the API is usable
 * immediately (e.g. in Postman/curl) without manual setup.
 * CommandLineRunner beans run once, right after the ApplicationContext
 * has fully started.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final EmployeeRepository repository;

    public DataSeeder(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return; // already seeded (e.g. on a restart against a persistent DB)
        }

        repository.save(new Employee("Aditi Sharma", "aditi.sharma@example.com", "Engineering",
                new BigDecimal("75000.00"), EmployeeType.FULL_TIME, LocalDate.of(2019, 3, 15)));

        repository.save(new Employee("Rohan Mehta", "rohan.mehta@example.com", "Engineering",
                new BigDecimal("95000.00"), EmployeeType.FULL_TIME, LocalDate.of(2016, 6, 1)));

        repository.save(new Employee("Sara Khan", "sara.khan@example.com", "Design",
                new BigDecimal("48000.00"), EmployeeType.CONTRACT, LocalDate.of(2021, 7, 1)));

        repository.save(new Employee("Priya Nair", "priya.nair@example.com", "Marketing",
                new BigDecimal("15000.00"), EmployeeType.INTERN, LocalDate.of(2024, 1, 10)));

        repository.save(new Employee("Neha Gupta", "neha.gupta@example.com", "Finance",
                new BigDecimal("110000.00"), EmployeeType.FULL_TIME, LocalDate.of(2014, 9, 5)));
    }
}
