package com.ems.service;

import com.ems.dto.EmployeeRequestDTO;
import com.ems.dto.EmployeeResponseDTO;
import com.ems.exception.DuplicateResourceException;
import com.ems.exception.ResourceNotFoundException;
import com.ems.mapper.EmployeeMapper;
import com.ems.model.Employee;
import com.ems.model.EmployeeType;
import com.ems.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Service marks this as a business-logic bean, picked up by component
 * scanning and made available for @Autowired / constructor injection
 * anywhere in the ApplicationContext.
 *
 * Dependencies are injected via constructor (the recommended approach
 * over field @Autowired - it makes the class trivially testable and
 * the dependencies explicit/final).
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    public EmployeeServiceImpl(EmployeeRepository repository, EmployeeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getAllEmployees() {
        return repository.findAll().stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forEmployee(id));
        return mapper.toResponseDto(employee);
    }

    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("An employee with email '" + dto.getEmail() + "' already exists");
        }
        Employee saved = repository.save(mapper.toEntity(dto));
        return mapper.toResponseDto(saved);
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {
        Employee existing = repository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forEmployee(id));

        // If the email is changing, make sure it doesn't collide with someone else's
        if (!existing.getEmail().equalsIgnoreCase(dto.getEmail()) && repository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("An employee with email '" + dto.getEmail() + "' already exists");
        }

        mapper.updateEntityFromDto(existing, dto);
        Employee saved = repository.save(existing);
        return mapper.toResponseDto(saved);
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!repository.existsById(id)) {
            throw ResourceNotFoundException.forEmployee(id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getByDepartment(String department) {
        return repository.findByDepartmentIgnoreCase(department).stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getBySalaryGreaterThan(BigDecimal salary) {
        return repository.findBySalaryGreaterThan(salary).stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getByEmployeeType(EmployeeType employeeType) {
        return repository.findByEmployeeType(employeeType).stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageSalaryByDepartment(String department) {
        Double average = repository.findAverageSalaryByDepartment(department);
        return average != null ? average : 0.0;
    }
}
