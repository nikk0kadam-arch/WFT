package com.ems.repository;

import com.ems.model.Employee;
import com.ems.model.EmployeeType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @DataJpaTest spins up an in-memory H2 context with just the JPA
 * layer (no web/security) and wraps each test in a rolled-back
 * transaction, so tests never leak data into each other.
 */
@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repository;

    @Test
    void findByDepartmentIgnoreCase_matchesRegardlessOfCase() {
        repository.save(new Employee("Test User", "test.user@example.com", "Engineering",
                new BigDecimal("60000"), EmployeeType.FULL_TIME, LocalDate.of(2020, 1, 1)));

        List<Employee> results = repository.findByDepartmentIgnoreCase("engineering");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getEmail()).isEqualTo("test.user@example.com");
    }

    @Test
    void findBySalaryGreaterThan_excludesLowerSalaries() {
        repository.save(new Employee("Low Earner", "low@example.com", "Sales",
                new BigDecimal("30000"), EmployeeType.CONTRACT, LocalDate.of(2022, 1, 1)));
        repository.save(new Employee("High Earner", "high@example.com", "Sales",
                new BigDecimal("90000"), EmployeeType.FULL_TIME, LocalDate.of(2018, 1, 1)));

        List<Employee> results = repository.findBySalaryGreaterThan(new BigDecimal("50000"));

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getEmail()).isEqualTo("high@example.com");
    }

    @Test
    void existsByEmail_reflectsCurrentState() {
        assertThat(repository.existsByEmail("nobody@example.com")).isFalse();

        repository.save(new Employee("Somebody", "somebody@example.com", "HR",
                new BigDecimal("40000"), EmployeeType.INTERN, LocalDate.of(2023, 5, 1)));

        assertThat(repository.existsByEmail("somebody@example.com")).isTrue();
    }
}
