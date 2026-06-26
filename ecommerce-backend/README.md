# E-Commerce Backend System

A scalable, secure microservices-based backend for an e-commerce platform built with Spring Boot 3.x, Spring Cloud, PostgreSQL, and Docker.

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                     API Gateway (Spring Cloud Gateway)           │
├─────────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐           │
│  │ User Service │  │ Product      │  │ Cart Service │           │
│  │              │  │ Catalog      │  │              │           │
│  └──────────────┘  └──────────────┘  └──────────────┘           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐           │
│  │ Order Svc    │  │ Payment Svc  │  │ Inventory    │           │
│  │              │  │              │  │ Service      │           │
│  └──────────────┘  └──────────────┘  └──────────────┘           │
│  ┌──────────────────────────────────────────────────┐           │
│  │        Notification Service (Kafka)              │           │
│  └──────────────────────────────────────────────────┘           │
├─────────────────────────────────────────────────────────────────┤
│   Eureka Server (Service Discovery)                              │
│   Config Server (Centralized Configuration)                      │
├─────────────────────────────────────────────────────────────────┤
│   PostgreSQL Database | Redis Cache | Kafka Message Broker      │
└─────────────────────────────────────────────────────────────────┘
```

## Services

### 1. **Eureka Server**
- Service discovery and registration
- Enables dynamic service-to-service communication

### 2. **Config Server**
- Centralized configuration management
- Environment-specific settings (dev, staging, prod)

### 3. **API Gateway**
- Single entry point for all external clients
- Request routing and load balancing
- Rate limiting and authentication

### 4. **User Service**
- User registration and login
- JWT-based authentication
- Role-based access control (RBAC)
- Profile management

### 5. **Product Catalog Service**
- Product CRUD operations
- Category management
- Search and filtering
- Inventory status tracking

### 6. **Cart Service**
- Add/update/remove cart items
- User-specific cart management
- Cart persistence

### 7. **Order Service**
- Order creation and processing
- Order history and tracking
- Order status management

### 8. **Payment Service**
- Dummy payment gateway integration
- Payment processing
- Transaction logging
- Order-payment linking

### 9. **Inventory Service**
- Stock level management
- Inventory updates after orders
- Low-stock alerts

### 10. **Notification Service**
- Kafka-based event processing
- Email notifications
- Order status notifications

### 11. **Shared Library**
- Common DTOs and utilities
- Shared constants and exceptions
- Common utilities and helpers

## Project Structure

```
ecommerce-backend/
├── pom.xml (Parent POM)
├── shared-library/
│   └── pom.xml
├── eureka-server/
│   ├── pom.xml
│   └── src/
├── config-server/
│   ├── pom.xml
│   └── src/
├── api-gateway/
│   ├── pom.xml
│   └── src/
├── user-service/
│   ├── pom.xml
│   └── src/
├── product-catalog-service/
│   ├── pom.xml
│   └── src/
├── cart-service/
│   ├── pom.xml
│   └── src/
├── order-service/
│   ├── pom.xml
│   └── src/
├── payment-service/
│   ├── pom.xml
│   └── src/
├── inventory-service/
│   ├── pom.xml
│   └── src/
├── notification-service/
│   ├── pom.xml
│   └── src/
├── docker-compose.yml
├── .env
└── README.md
```

## Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Java | Java | 17+ |
| Framework | Spring Boot | 3.2.0 |
| Cloud | Spring Cloud | 2023.0.0 |
| Security | Spring Security + JWT | - |
| Database | PostgreSQL | 14+ |
| API Docs | SpringDoc OpenAPI | 2.1.0 |
| Mapping | MapStruct | 1.5.5 |
| Containerization | Docker | Latest |
| Orchestration | Docker Compose | - |

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- Docker & Docker Compose
- PostgreSQL 14+

## Quick Start

### 1. Clone and Navigate
```bash
cd ecommerce-backend
```

### 2. Build All Services
```bash
mvn clean install
```

### 3. Start Infrastructure (Docker Compose)
```bash
docker-compose up -d
```

This starts:
- PostgreSQL (port 5432)

### 4. Start Services in Order
```bash
# Terminal 1: Eureka Server (port 8761)
cd eureka-server && mvn spring-boot:run

# Terminal 2: Config Server (port 8888)
cd config-server && mvn spring-boot:run

# Terminal 3: API Gateway (port 8080)
cd api-gateway && mvn spring-boot:run

# Terminal 4+: Individual Services
cd user-service && mvn spring-boot:run        # 8001
cd product-catalog-service && mvn spring-boot:run # 8002
cd cart-service && mvn spring-boot:run        # 8003
cd order-service && mvn spring-boot:run       # 8004
cd payment-service && mvn spring-boot:run     # 8005
cd inventory-service && mvn spring-boot:run   # 8006
cd notification-service && mvn spring-boot:run # 8007
```

### 5. Access Services

| Service | URL |
|---------|-----|
| Eureka Dashboard | http://localhost:8761 |
| API Gateway | http://localhost:8080 |
| Swagger UI (API Gateway) | http://localhost:8080/swagger-ui.html |
| Config Server | http://localhost:8888 |

## Configuration

### Environment Variables (.env)
```
# Database
DB_URL=jdbc:postgresql://localhost:5432/ecommerce
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# Kafka
KAFKA_BOOTSTRAP_SERVERS=localhost:9092

# JWT
JWT_SECRET=your-secret-key-min-256-bits
JWT_EXPIRATION=86400000
```

### Service Configuration
Each service has its own `application.yml` in `src/main/resources/`:
- Database configuration
- Eureka client settings
- JWT configuration (for services that need it)
- Logging settings

## Development Workflow

### Adding a New Microservice
1. Create a new directory: `new-service/`
2. Create `pom.xml` with parent reference
3. Create `src/main/java` and `src/test/java` directories
4. Create `application.yml` in `src/main/resources/`
5. Add module to parent `pom.xml`

### Building Individual Service
```bash
cd service-name
mvn clean install
mvn spring-boot:run
```

### Building All Services
```bash
mvn clean install -DskipTests
```

## Docker Deployment

### Build Docker Images
```bash
docker-compose build
```

### Run with Docker Compose
```bash
docker-compose up
```

### Stop Services
```bash
docker-compose down
```

## Testing

### Run Unit Tests
```bash
mvn test
```

### Run Integration Tests
```bash
mvn verify
```

### Run Specific Service Tests
```bash
cd user-service
mvn test
```

## API Documentation

Swagger/OpenAPI documentation is available at:
- http://localhost:8080/swagger-ui.html (through API Gateway)
- Individual service Swagger: http://localhost:PORT/swagger-ui.html

## Common Issues & Solutions

### Services not registering with Eureka
- Check if Eureka Server is running on port 8761
- Verify network connectivity between services
- Check service logs for connection errors

### Database connection errors
- Verify PostgreSQL is running
- Check DB credentials in application.yml
- Ensure database exists

### JWT token expiration
- Token validity: 24 hours (configurable)
- Refresh tokens: Implement refresh token endpoint in User Service

## Deployment to Cloud

### Azure (Recommended)
- Use Azure Container Registry (ACR) for images
- Deploy to Azure Container Instances or AKS
- Use Azure Database for PostgreSQL
- Use Azure Cache for Redis

### AWS
- Push images to Amazon ECR
- Deploy to ECS or EKS
- Use RDS for PostgreSQL
- Use ElastiCache for Redis

## Performance Optimization

1. **Caching**: Redis for frequently accessed data
2. **Async Processing**: Kafka for event-driven operations
3. **Database**: Use indexes, connection pooling
4. **API Gateway**: Rate limiting, request/response compression
5. **Monitoring**: Spring Boot Actuator, Micrometer

## Security Best Practices

1. Store secrets in environment variables or vault
2. Use HTTPS in production
3. Implement rate limiting
4. Validate all inputs
5. Use parameterized queries
6. Implement request/response logging
7. Regular dependency updates

## Contributing

1. Create a feature branch
2. Follow existing code style
3. Write tests for new features
4. Submit a pull request

## License

MIT License - See LICENSE file for details

## Support

For issues and questions:
- Check logs: `docker-compose logs -f service-name`
- Review application.yml configuration
- Check Eureka dashboard for service status
- Review Swagger API documentation

---

**Ready to start building!** Next steps:
1. Set up environment variables
2. Start Docker Compose infrastructure
3. Begin developing individual services
