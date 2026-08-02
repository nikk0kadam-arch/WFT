package com.ems.controller;

import com.ems.dto.EmployeeRequestDTO;
import com.ems.dto.EmployeeResponseDTO;
import com.ems.model.EmployeeType;
import com.ems.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @RestController = @Controller + @ResponseBody: every method's return
 * value is written straight to the HTTP response body as JSON (via the
 * Jackson converter auto-configured by spring-boot-starter-web),
 * instead of being resolved to a view name.
 *
 * All endpoints here are also gated by SecurityConfig: GETs require
 * USER or ADMIN, writes (POST/PUT/DELETE) require ADMIN.
 */
@RestController
@RequestMapping("/api/employees")
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * GET /api/employees
     * GET /api/employees?department=Engineering
     * GET /api/employees?minSalary=50000
     * GET /api/employees?type=FULL_TIME
     * Optional query params narrow the result set; combining more than
     * one filter at a time is intentionally not supported here to keep
     * the derived-query mapping simple and explicit.
     */
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) EmployeeType type) {

        if (department != null) {
            return ResponseEntity.ok(employeeService.getByDepartment(department));
        }
        if (minSalary != null) {
            return ResponseEntity.ok(employeeService.getBySalaryGreaterThan(minSalary));
        }
        if (type != null) {
            return ResponseEntity.ok(employeeService.getByEmployeeType(type));
        }
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // GET /api/employees/{id}
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    // GET /api/employees/department/{department}/average-salary
    @GetMapping("/department/{department}/average-salary")
    public ResponseEntity<Map<String, Object>> getAverageSalary(@PathVariable String department) {
        Double average = employeeService.getAverageSalaryByDepartment(department);
        return ResponseEntity.ok(Map.of("department", department, "averageSalary", average));
    }

    // POST /api/employees -> 201 Created with a Location header pointing at the new resource
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO request,
                                                               UriComponentsBuilder uriBuilder) {
        EmployeeResponseDTO created = employeeService.createEmployee(request);
        var location = uriBuilder.path("/api/employees/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    // PUT /api/employees/{id} -> 200 OK with the updated resource
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Long id,
                                                               @Valid @RequestBody EmployeeRequestDTO request) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    // DELETE /api/employees/{id} -> 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/employees/salary/greater-than?amount=50000 (path-variable-style alternative to the query filter above)
    @GetMapping("/salary/greater-than")
    public ResponseEntity<List<EmployeeResponseDTO>> getBySalaryGreaterThan(
            @RequestParam @DecimalMin(value = "0.0", message = "amount must not be negative") BigDecimal amount) {
        return ResponseEntity.ok(employeeService.getBySalaryGreaterThan(amount));
    }
}
