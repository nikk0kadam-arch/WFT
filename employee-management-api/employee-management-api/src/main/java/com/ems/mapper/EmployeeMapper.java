package com.ems.mapper;

import com.ems.dto.EmployeeRequestDTO;
import com.ems.dto.EmployeeResponseDTO;
import com.ems.model.Employee;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

/**
 * Converts between the Employee entity and its request/response DTOs.
 * Kept as a small @Component rather than static methods so it can be
 * mocked/injected in tests if the mapping logic grows.
 */
@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeRequestDTO dto) {
        return new Employee(
                dto.getName(),
                dto.getEmail(),
                dto.getDepartment(),
                dto.getSalary(),
                dto.getEmployeeType(),
                dto.getJoiningDate()
        );
    }

    public void updateEntityFromDto(Employee employee, EmployeeRequestDTO dto) {
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());
        employee.setEmployeeType(dto.getEmployeeType());
        employee.setJoiningDate(dto.getJoiningDate());
    }

    public EmployeeResponseDTO toResponseDto(Employee employee) {
        int experience = Period.between(employee.getJoiningDate(), LocalDate.now()).getYears();
        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment(),
                employee.getSalary(),
                employee.getEmployeeType(),
                employee.getJoiningDate(),
                experience,
                employee.getCreatedAt()
        );
    }
}
