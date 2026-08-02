# Employee Management System (Core Java)

A menu-driven console application demonstrating Core Java, OOP, Java 8,
and classic design patterns.

## How to Compile & Run

From the project root (the folder containing `src/` and `data/`):

```bash
# Compile everything into an "out" directory
find src -name "*.java" > sources.txt
javac -d out @sources.txt

# Run (the app looks for data/employees.txt relative to the working directory)
java -cp out com.ems.app.Main
```

On Windows (PowerShell), replace the `find` line with:
```powershell
Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName } | Out-File sources.txt
javac -d out "@sources.txt"
```

Requires JDK 17+ (uses `switch` arrow syntax, `Period`, `Files`/`Path` NIO.2,
and `String.isBlank()`).

## Project Structure

```
EmployeeManagementSystem/
├── data/
│   └── employees.txt              # Seed data (pipe-delimited)
├── src/com/ems/
│   ├── model/
│   │   ├── Employee.java          # abstract base (fields, ctors, LocalDate/Period)
│   │   ├── FullTimeEmployee.java
│   │   ├── ContractEmployee.java
│   │   └── InternEmployee.java
│   ├── exception/
│   │   ├── EmployeeNotFoundException.java
│   │   └── InvalidEmployeeDataException.java
│   ├── strategy/
│   │   ├── SalaryStrategy.java        # Strategy Pattern interface
│   │   ├── MonthlySalaryStrategy.java
│   │   ├── HourlyWageStrategy.java
│   │   └── StipendStrategy.java
│   ├── factory/
│   │   └── EmployeeFactory.java       # Factory Method Pattern
│   ├── repository/
│   │   └── EmployeeRepository.java    # Map/List/Set storage
│   ├── service/
│   │   ├── EmployeeService.java       # business logic, streams, validation
│   │   └── EmployeeDTO.java
│   ├── util/
│   │   └── EmployeeFileUtil.java      # NIO.2 file read/write
│   └── app/
│       ├── DataLoader.java            # Singleton Pattern
│       └── Main.java                  # CLI menu (entry point)
└── README.md
```

## Where Each Concept Lives

| Concept | Where |
|---|---|
| Variables, data types, math operators | `Employee` fields; `MonthlySalaryStrategy`/`HourlyWageStrategy` bonus & wage math |
| Control flow (if/else, switch, loops) | `Main` menu switch, input validation loops, `EmployeeFileUtil` line loop |
| Methods (overloading) | `Employee` constructors, `EmployeeService.addEmployee(...)` (2 overloads), `calculateBonus(...)` (2 overloads) |
| Strings & text APIs | `Employee.toString()` formatting, `EmployeeService.log()` StringBuilder |
| OOP essentials | `Employee` hierarchy, packages per layer |
| Encapsulation | Private fields + getters/setters throughout `model` |
| Exceptions | `EmployeeNotFoundException`, `InvalidEmployeeDataException`, try/catch in `EmployeeFileUtil` and `Main` |
| Collections | `Map` in `EmployeeRepository`, `List<Employee>`, `Set<String>` departments, `Comparator` sorting |
| Basic I/O / NIO.2 | `EmployeeFileUtil` (`Files.readAllLines`, `Files.write`) |
| Java 8 (lambdas/streams/date-time) | `EmployeeService` stream filters/maps, `Employee.getExperienceInYears()` (`Period`) |
| Singleton | `DataLoader` |
| Factory Method | `EmployeeFactory` |
| Strategy | `SalaryStrategy` + 3 implementations |

## Sample Session

```
Employee Management System
===========================
[INFO] Loaded 7 employee record(s) from data/employees.txt

1.  Add Employee
2.  Update Employee (department / age)
...
Choose an option: 7
[101 ] Aditi Sharma        | Age:29  | Dept:Engineering  | Type:FULLTIME  | Joined:2019-03-15 | Exp: 7y | Salary:   82500.00
...
```
