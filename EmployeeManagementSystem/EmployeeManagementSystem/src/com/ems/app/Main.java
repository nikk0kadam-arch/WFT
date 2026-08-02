package com.ems.app;

import com.ems.exception.EmployeeNotFoundException;
import com.ems.exception.InvalidEmployeeDataException;
import com.ems.model.Employee;
import com.ems.repository.EmployeeRepository;
import com.ems.service.EmployeeDTO;
import com.ems.service.EmployeeService;
import com.ems.util.EmployeeFileUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Menu-driven Command Line Interface for the Employee Management System.
 * Wires together the Repository, Service, Singleton DataLoader, and
 * Factory/Strategy patterns behind a simple text menu.
 */
public class Main {

    private static final Path DATA_FILE = Paths.get("data", "employees.txt");
    private static final Scanner SCANNER = new Scanner(System.in);

    private static final EmployeeRepository repository = new EmployeeRepository();
    private static final EmployeeService service = new EmployeeService(repository);

    public static void main(String[] args) {
        System.out.println("Employee Management System");
        System.out.println("===========================");

        // Singleton: guaranteed to load the initial data set only once.
        DataLoader.getInstance().loadInitialData(repository, DATA_FILE);

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");

            try {
                switch (choice) {
                    case 1 -> handleAddEmployee();
                    case 2 -> handleUpdateEmployee();
                    case 3 -> handleDeleteEmployee();
                    case 4 -> handleSearchById();
                    case 5 -> handleSearchByName();
                    case 6 -> handleSearchByDepartment();
                    case 7 -> handleListAll();
                    case 8 -> handleSortBySalary();
                    case 9 -> handleFilterBySalary();
                    case 10 -> handleShowDepartments();
                    case 11 -> handleCalculateBonus();
                    case 12 -> handleSummaryReport();
                    case 13 -> handleSaveToFile();
                    case 0 -> {
                        running = false;
                        System.out.println("Goodbye!");
                    }
                    default -> System.out.println("Invalid option. Please choose again.");
                }
            } catch (EmployeeNotFoundException | InvalidEmployeeDataException businessError) {
                System.out.println("[ERROR] " + businessError.getMessage());
            } catch (Exception unexpected) {
                System.out.println("[ERROR] Something went wrong: " + unexpected.getMessage());
            }
        }

        SCANNER.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1.  Add Employee");
        System.out.println("2.  Update Employee (department / age)");
        System.out.println("3.  Delete Employee");
        System.out.println("4.  Search by ID");
        System.out.println("5.  Search by Name");
        System.out.println("6.  Search by Department");
        System.out.println("7.  List All Employees");
        System.out.println("8.  List Sorted by Salary (desc)");
        System.out.println("9.  Filter by Salary Greater Than X");
        System.out.println("10. Show Distinct Departments");
        System.out.println("11. Calculate Bonus for an Employee");
        System.out.println("12. Generate Summary Report");
        System.out.println("13. Save All Employees to File");
        System.out.println("0.  Exit");
    }

    // ---- Handlers ----

    private static void handleAddEmployee() {
        System.out.println("Employee type: 1) Full-Time  2) Contract  3) Intern");
        int typeChoice = readInt("Choose type: ");

        int id = readInt("Employee ID: ");
        String name = readNonBlankString("Name: ");
        int age = readInt("Age: ");
        String department = readNonBlankString("Department: ");
        LocalDate joiningDate = readDate("Joining date (YYYY-MM-DD): ");

        String type;
        double amount1;
        double amount2;

        switch (typeChoice) {
            case 1 -> {
                type = "FULLTIME";
                amount1 = readDouble("Base monthly salary: ");
                amount2 = readDouble("Bonus percent (e.g. 10 for 10%): ");
            }
            case 2 -> {
                type = "CONTRACT";
                amount1 = readDouble("Hourly rate: ");
                amount2 = readDouble("Hours worked this month: ");
            }
            case 3 -> {
                type = "INTERN";
                amount1 = readDouble("Monthly stipend: ");
                amount2 = 0.0;
            }
            default -> throw new InvalidEmployeeDataException("Invalid employee type choice: " + typeChoice);
        }

        Employee created = service.addEmployee(type, id, name, age, department, joiningDate, amount1, amount2);
        System.out.println("Created: " + created);
    }

    private static void handleUpdateEmployee() {
        int id = readInt("Employee ID to update: ");
        System.out.println("1) Update department  2) Update age");
        int fieldChoice = readInt("Choose field: ");

        Employee updated;
        if (fieldChoice == 1) {
            String newDept = readNonBlankString("New department: ");
            updated = service.updateDepartment(id, newDept);
        } else if (fieldChoice == 2) {
            int newAge = readInt("New age: ");
            updated = service.updateAge(id, newAge);
        } else {
            System.out.println("Invalid field choice.");
            return;
        }
        System.out.println("Updated: " + updated);
    }

    private static void handleDeleteEmployee() {
        int id = readInt("Employee ID to delete: ");
        service.deleteEmployee(id);
        System.out.println("Employee " + id + " deleted.");
    }

    private static void handleSearchById() {
        int id = readInt("Employee ID: ");
        Employee employee = service.getById(id);
        System.out.println(employee);
    }

    private static void handleSearchByName() {
        String namePart = readNonBlankString("Name (or part of it) to search: ");
        List<Employee> results = service.searchByName(namePart);
        printEmployeeList(results);
    }

    private static void handleSearchByDepartment() {
        String department = readNonBlankString("Department: ");
        List<Employee> results = service.searchByDepartment(department);
        printEmployeeList(results);
    }

    private static void handleListAll() {
        printEmployeeList(service.getAll());
    }

    private static void handleSortBySalary() {
        printEmployeeList(service.sortBySalaryDescending());
    }

    private static void handleFilterBySalary() {
        double threshold = readDouble("Show employees with salary greater than: ");
        printEmployeeList(service.filterBySalaryGreaterThan(threshold));
    }

    private static void handleShowDepartments() {
        Set<String> departments = service.getDistinctDepartments();
        System.out.println("Distinct departments (" + departments.size() + "): " + departments);
    }

    private static void handleCalculateBonus() {
        int id = readInt("Employee ID: ");
        Employee employee = service.getById(id);
        double percent = readDouble("Bonus percent: ");
        double bonus = service.calculateBonus(employee, percent);
        System.out.printf("Bonus for %s at %.2f%%: %.2f%n", employee.getName(), percent, bonus);
    }

    private static void handleSummaryReport() {
        System.out.println(service.generateSummaryReport());
        List<EmployeeDTO> dtos = service.toDTOList();
        System.out.println("--- DTO projection (id, name, department, salary) ---");
        dtos.forEach(System.out::println);
    }

    private static void handleSaveToFile() {
        EmployeeFileUtil.writeEmployees(DATA_FILE, service.getAll());
    }

    private static void printEmployeeList(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        for (Employee e : employees) {
            System.out.println(e);
        }
        System.out.println("(" + employees.size() + " employee(s))");
    }

    // ---- Input helpers (validation + exception handling) ----

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readNonBlankString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("This field cannot be blank.");
        }
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine().trim();
            try {
                return LocalDate.parse(line);
            } catch (DateTimeParseException e) {
                System.out.println("Please use the format YYYY-MM-DD.");
            }
        }
    }
}
