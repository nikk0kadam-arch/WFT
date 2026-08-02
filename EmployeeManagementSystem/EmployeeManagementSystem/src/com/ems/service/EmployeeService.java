package com.ems.service;

import com.ems.exception.InvalidEmployeeDataException;
import com.ems.factory.EmployeeFactory;
import com.ems.model.Employee;
import com.ems.repository.EmployeeRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service layer: houses business logic, validation, and query operations
 * on top of the EmployeeRepository. This is the layer the CLI (Main)
 * talks to; it never touches the Map directly.
 */
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    // ---- Create ----

    public Employee addEmployee(String type, int id, String name, int age, String department,
                                 LocalDate joiningDate, double amount1, double amount2) {
        validateNewEmployee(id, name, age);
        Employee employee = EmployeeFactory.createEmployee(type, id, name, age, department, joiningDate, amount1, amount2);
        repository.save(employee);
        log("Added", employee);
        return employee;
    }

    // Overloaded convenience method: department defaults to "General"
    public Employee addEmployee(String type, int id, String name, int age,
                                 LocalDate joiningDate, double amount1, double amount2) {
        return addEmployee(type, id, name, age, "General", joiningDate, amount1, amount2);
    }

    private void validateNewEmployee(int id, String name, int age) {
        if (repository.existsById(id)) {
            throw new InvalidEmployeeDataException("An employee with id " + id + " already exists");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidEmployeeDataException("Employee name cannot be blank");
        }
        if (age < 18 || age > 70) {
            throw new InvalidEmployeeDataException("Employee age must be between 18 and 70 (got " + age + ")");
        }
    }

    // ---- Read ----

    public Employee getById(int id) {
        return repository.findById(id);
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }

    // ---- Update ----

    public Employee updateDepartment(int id, String newDepartment) {
        Employee employee = repository.findById(id);
        employee.setDepartment(newDepartment);
        log("Updated department for", employee);
        return employee;
    }

    public Employee updateAge(int id, int newAge) {
        if (newAge < 18 || newAge > 70) {
            throw new InvalidEmployeeDataException("Employee age must be between 18 and 70 (got " + newAge + ")");
        }
        Employee employee = repository.findById(id);
        employee.setAge(newAge);
        log("Updated age for", employee);
        return employee;
    }

    // ---- Delete ----

    public void deleteEmployee(int id) {
        Employee employee = repository.findById(id);
        repository.deleteById(id);
        log("Deleted", employee);
    }

    // ---- Search (Java 8 Streams) ----

    public List<Employee> searchByName(String namePart) {
        String needle = namePart.toLowerCase();
        return repository.findAll().stream()
                .filter(e -> e.getName().toLowerCase().contains(needle))
                .collect(Collectors.toList());
    }

    public List<Employee> searchByDepartment(String department) {
        return repository.findAll().stream()
                .filter(e -> e.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }

    public List<Employee> filterBySalaryGreaterThan(double threshold) {
        return repository.findAll().stream()
                .filter(e -> e.calculateSalary() > threshold)
                .sorted(Comparator.comparingDouble(Employee::calculateSalary).reversed())
                .collect(Collectors.toList());
    }

    public List<Employee> sortBySalaryDescending() {
        return repository.findAll().stream()
                .sorted(Comparator.comparingDouble(Employee::calculateSalary).reversed())
                .collect(Collectors.toList());
    }

    public List<Employee> sortByNameThenDepartment() {
        Comparator<Employee> byName = Comparator.comparing(Employee::getName, String.CASE_INSENSITIVE_ORDER);
        Comparator<Employee> byDept = Comparator.comparing(Employee::getDepartment, String.CASE_INSENSITIVE_ORDER);
        return repository.findAll().stream()
                .sorted(byName.thenComparing(byDept))
                .collect(Collectors.toList());
    }

    public Set<String> getDistinctDepartments() {
        return repository.getDistinctDepartments();
    }

    // Maps domain Employees to lightweight DTOs (Java 8 Stream.map)
    public List<EmployeeDTO> toDTOList() {
        return repository.findAll().stream()
                .map(e -> new EmployeeDTO(e.getId(), e.getName(), e.getDepartment(), e.calculateSalary()))
                .collect(Collectors.toList());
    }

    // ---- Method overloading: calculateBonus ----

    /** Flat percentage bonus on top of the employee's computed salary. */
    public double calculateBonus(Employee employee, double percent) {
        return employee.calculateSalary() * (percent / 100.0);
    }

    /** Fixed bonus amount, capped so it never exceeds the given max. */
    public double calculateBonus(Employee employee, double percent, double maxAmount) {
        double bonus = calculateBonus(employee, percent);
        return Math.min(bonus, maxAmount);
    }

    // ---- Reporting (StringBuilder + String formatting) ----

    public String generateSummaryReport() {
        List<Employee> all = repository.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("===== Employee Summary Report =====\n");
        sb.append("Total employees: ").append(all.size()).append('\n');

        double totalPayroll = all.stream().mapToDouble(Employee::calculateSalary).sum();
        double avgSalary = all.isEmpty() ? 0.0 : totalPayroll / all.size();

        sb.append(String.format("Total monthly payroll: %.2f%n", totalPayroll));
        sb.append(String.format("Average salary: %.2f%n", avgSalary));
        sb.append("Departments: ").append(getDistinctDepartments()).append('\n');
        sb.append("====================================");
        return sb.toString();
    }

    private void log(String action, Employee employee) {
        StringBuilder message = new StringBuilder();
        message.append("[LOG] ").append(action).append(" employee -> ")
                .append("id=").append(employee.getId())
                .append(", name=").append(employee.getName())
                .append(", type=").append(employee.getEmployeeType());
        System.out.println(message);
    }
}
