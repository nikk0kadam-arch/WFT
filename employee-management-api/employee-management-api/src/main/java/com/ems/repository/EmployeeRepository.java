package com.ems.repository;

import com.ems.model.Employee;
import com.ems.model.EmployeeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository. Extending JpaRepository gives us CRUD
 * (save, findById, findAll, deleteById, ...) for free via a dynamic
 * proxy Spring generates at startup - no implementation class needed.
 *
 * Method names below are "derived queries": Spring Data parses the
 * method name and builds the query automatically.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Derived query: SELECT * FROM employees WHERE department = ? (case-insensitive)
    List<Employee> findByDepartmentIgnoreCase(String department);

    // Derived query: SELECT * FROM employees WHERE salary > ?
    List<Employee> findBySalaryGreaterThan(BigDecimal salary);

    // Derived query: SELECT * FROM employees WHERE employee_type = ?
    List<Employee> findByEmployeeType(EmployeeType employeeType);

    // Derived query: SELECT * FROM employees WHERE email = ?
    Optional<Employee> findByEmail(String email);

    // Derived query: existence check, avoids loading the full row
    boolean existsByEmail(String email);

    // Explicit JPQL: combine two conditions with a named-parameter query
    @Query("SELECT e FROM Employee e WHERE e.department = :department AND e.salary > :minSalary")
    List<Employee> findByDepartmentAndMinSalary(@Param("department") String department,
                                                 @Param("minSalary") BigDecimal minSalary);

    // Explicit JPQL: aggregate function
    @Query("SELECT AVG(e.salary) FROM Employee e WHERE e.department = :department")
    Double findAverageSalaryByDepartment(@Param("department") String department);
}
