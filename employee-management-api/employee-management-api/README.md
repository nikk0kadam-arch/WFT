# Employee Management REST API (Spring Boot)

A Spring Boot 3 / Java 17 REST API for managing employees, with validation,
Spring Data JPA persistence, a global exception handler, HTTP Basic security,
and Actuator health/info endpoints.

## Requirements

- JDK 17+
- Maven 3.8+ (or use the wrapper if you generate one via `mvn -N wrapper:wrapper`)
- No external DB needed by default — runs on an in-memory H2 database.

## Run it

```bash
mvn spring-boot:run
```

The app starts on `http://localhost:8080` and seeds 5 sample employees on boot.

To build a runnable jar instead:
```bash
mvn clean package
java -jar target/employee-management-api-0.0.1-SNAPSHOT.jar
```

## Credentials (in-memory users)

| Username | Password | Roles |
|---|---|---|
| `user`  | `user123`  | USER |
| `admin` | `admin123` | USER, ADMIN |

- **GET** endpoints → require `USER` or `ADMIN`
- **POST / PUT / DELETE** endpoints → require `ADMIN`

## API Reference

Base path: `/api/employees`

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/api/employees` | USER/ADMIN | List all employees |
| GET | `/api/employees?department=Engineering` | USER/ADMIN | Filter by department |
| GET | `/api/employees?minSalary=50000` | USER/ADMIN | Filter by salary greater than |
| GET | `/api/employees?type=FULL_TIME` | USER/ADMIN | Filter by employee type |
| GET | `/api/employees/{id}` | USER/ADMIN | Get one employee (404 if missing) |
| GET | `/api/employees/salary/greater-than?amount=50000` | USER/ADMIN | Same filter, dedicated endpoint |
| GET | `/api/employees/department/{department}/average-salary` | USER/ADMIN | JPQL aggregate query |
| POST | `/api/employees` | ADMIN | Create (201 + `Location` header) |
| PUT | `/api/employees/{id}` | ADMIN | Update (200, 404 if missing) |
| DELETE | `/api/employees/{id}` | ADMIN | Delete (204, 404 if missing) |
| GET | `/actuator/health` | public | Health check |
| GET | `/actuator/info` | public | Build/app info |
| `/h2-console` | — | public (dev only) | H2 web console, JDBC URL `jdbc:h2:mem:emsdb` |

### Example requests

Create an employee (ADMIN):
```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
        "name": "Vikram Rao",
        "email": "vikram.rao@example.com",
        "department": "Operations",
        "salary": 62000.00,
        "employeeType": "CONTRACT",
        "joiningDate": "2022-04-11"
      }'
```

List employees (USER or ADMIN):
```bash
curl -u user:user123 http://localhost:8080/api/employees
```

Try without credentials → `401 Unauthorized`.
Try a POST as `user` → `403 Forbidden` (authenticated, but not authorized).

### Validation error example

```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"name": "", "email": "not-an-email", "salary": -5}'
```

Response (`400 Bad Request`):
```json
{
  "timestamp": "2026-08-02T10:15:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "One or more fields are invalid",
  "path": "/api/employees",
  "validationErrors": {
    "name": "Name is required",
    "email": "Email must be a valid email address",
    "department": "Department is required",
    "salary": "Salary must be greater than 0",
    "employeeType": "Employee type is required",
    "joiningDate": "Joining date is required"
  }
}
```

## Switching to PostgreSQL

An `application-postgres.yml` profile is included. Start Postgres, create a
database, then run:

```bash
export DB_HOST=localhost DB_PORT=5432 DB_NAME=emsdb DB_USER=postgres DB_PASSWORD=postgres
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

## Running the tests

```bash
mvn test
```

Includes:
- a context-load smoke test
- `@DataJpaTest` repository tests (derived queries against a real in-memory H2 schema)
- `@WebMvcTest` controller tests covering auth (401), authorization (403),
  validation (400), and successful creation (201)

## Project Structure

```
employee-management-api/
├── pom.xml
├── src/main/resources/
│   ├── application.yml            # default profile (H2)
│   └── application-postgres.yml   # optional Postgres profile
└── src/main/java/com/ems/
    ├── EmployeeManagementApiApplication.java
    ├── config/
    │   ├── SecurityConfig.java     # Basic Auth, in-memory users, access rules
    │   └── DataSeeder.java         # CommandLineRunner, sample data on boot
    ├── model/
    │   ├── Employee.java           # @Entity
    │   └── EmployeeType.java       # enum
    ├── dto/
    │   ├── EmployeeRequestDTO.java # @Valid input, bean validation annotations
    │   └── EmployeeResponseDTO.java
    ├── mapper/
    │   └── EmployeeMapper.java     # entity <-> DTO conversion
    ├── repository/
    │   └── EmployeeRepository.java # JpaRepository + derived + JPQL queries
    ├── service/
    │   ├── EmployeeService.java        # interface
    │   └── EmployeeServiceImpl.java    # business logic, @Transactional
    ├── controller/
    │   └── EmployeeController.java # REST endpoints
    └── exception/
        ├── ResourceNotFoundException.java
        ├── DuplicateResourceException.java
        ├── ApiError.java
        └── GlobalExceptionHandler.java   # @RestControllerAdvice
```

## Where Each Concept Lives

| Concept | Where |
|---|---|
| IoC / DI, Beans, Autowiring | `@Service`/`@Repository`/`@Component` throughout; constructor injection everywhere; `@Bean` methods in `SecurityConfig` |
| Spring MVC (`@RestController`, mappings, JSON, validation) | `EmployeeController`, Jackson auto-configured by `spring-boot-starter-web` |
| REST CRUD + status codes + DTOs | `EmployeeController` (200/201/204/404/400), `EmployeeRequestDTO`/`EmployeeResponseDTO` |
| JPA & Hibernate (`@Entity`, derived queries, JPQL) | `Employee`, `EmployeeRepository` |
| Spring Boot essentials (starters, auto-config, H2, Actuator) | `pom.xml`, `application.yml`, `management.endpoints` |
| Spring Security (Basic Auth, in-memory users, authz) | `SecurityConfig` |

## Notes on production-readiness

This is a learning/demo project. Before shipping something like this for
real, you'd want to: move users into the database (not in-memory), add
pagination to `GET /api/employees`, consider `@Version` for optimistic
locking on updates, add rate limiting, and lock down `/h2-console` and
`ddl-auto` entirely outside of local dev.
