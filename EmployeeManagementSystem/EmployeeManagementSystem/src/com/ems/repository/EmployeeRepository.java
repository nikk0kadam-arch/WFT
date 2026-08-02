package com.ems.repository;

import com.ems.exception.EmployeeNotFoundException;
import com.ems.model.Employee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * In-memory data store for employees.
 * Uses a Map<Integer, Employee> for O(1) id lookups (LinkedHashMap preserves
 * insertion order for predictable listing/report output).
 */
public class EmployeeRepository {

    private final Map<Integer, Employee> employees = new LinkedHashMap<>();

    public Employee save(Employee employee) {
        employees.put(employee.getId(), employee);
        return employee;
    }

    public Employee findById(int id) {
        Employee employee = employees.get(id);
        if (employee == null) {
            throw new EmployeeNotFoundException(id);
        }
        return employee;
    }

    public boolean existsById(int id) {
        return employees.containsKey(id);
    }

    public void deleteById(int id) {
        if (!employees.containsKey(id)) {
            throw new EmployeeNotFoundException(id);
        }
        employees.remove(id);
    }

    public List<Employee> findAll() {
        return new ArrayList<>(employees.values());
    }

    public int count() {
        return employees.size();
    }

    /**
     * Set<String> of every distinct department currently in the repository
     * (departments are unique by nature of a Set).
     */
    public Set<String> getDistinctDepartments() {
        return employees.values().stream()
                .map(Employee::getDepartment)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public void clear() {
        employees.clear();
    }

    public void saveAll(Collection<Employee> newEmployees) {
        for (Employee e : newEmployees) {
            employees.put(e.getId(), e);
        }
    }
}
