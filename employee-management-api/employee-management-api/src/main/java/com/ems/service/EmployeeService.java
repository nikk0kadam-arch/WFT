package com.ems.service;

import com.ems.dto.EmployeeRequestDTO;
import com.ems.dto.EmployeeResponseDTO;
import com.ems.model.EmployeeType;

import java.math.BigDecimal;
import java.util.List;

/**
 * Business logic contract for employee operations. Depending on this
 * interface (rather than the impl) in the controller keeps the layers
 * loosely coupled and easy to mock in unit tests.
 */
public interface EmployeeService {

    List<EmployeeResponseDTO> getAllEmployees();

    EmployeeResponseDTO getEmployeeById(Long id);

    EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto);

    EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto);

    void deleteEmployee(Long id);

    List<EmployeeResponseDTO> getByDepartment(String department);

    List<EmployeeResponseDTO> getBySalaryGreaterThan(BigDecimal salary);

    List<EmployeeResponseDTO> getByEmployeeType(EmployeeType employeeType);

    Double getAverageSalaryByDepartment(String department);
}
