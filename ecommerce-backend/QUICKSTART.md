# Quick Start Guide - E-Commerce Backend System

## Prerequisites

- Java 17+ ✓
- Maven 3.8+ ✓
- Docker & Docker Compose ✓
- Git ✓

## Setup Steps

### 1. Navigate to Project Directory
```bash
cd ecommerce-backend
```

### 2. Start Infrastructure (PostgreSQL)
```bash
docker-compose up -d
```

Verify PostgreSQL is running:
```bash
docker-compose ps
```

### 3. Build All Services
```bash
mvn clean install -DskipTests
```

### 4. Run Services in Order

**Terminal 1: Eureka Server** (Service Discovery)
```bash
cd eureka-server
mvn spring-boot:run
```
Access: http://localhost:8761

**Terminal 2: Config Server** (Configuration Management)
```bash
cd config-server
mvn spring-boot:run
```
Access: http://localhost:8888

**Terminal 3: API Gateway** (Main entry point)
```bash
cd api-gateway
mvn spring-boot:run
```
Access: http://localhost:8080

**Terminal 4: User Service**
```bash
cd user-service
mvn spring-boot:run
```
Port: 8001

**Terminal 5: Product Catalog Service**
```bash
cd product-catalog-service
mvn spring-boot:run
```
Port: 8002

**Terminal 6: Cart Service**
```bash
cd cart-service
mvn spring-boot:run
```
Port: 8003

**Terminal 7: Order Service**
```bash
cd order-service
mvn spring-boot:run
```
Port: 8004

**Terminal 8: Payment Service**
```bash
cd payment-service
mvn spring-boot:run
```
Port: 8005

**Terminal 9: Inventory Service**
```bash
cd inventory-service
mvn spring-boot:run
```
Port: 8006

**Terminal 10: Notification Service**
```bash
cd notification-service
mvn spring-boot:run
```
Port: 8007

## Accessing Services

| Service | URL |
|---------|-----|
| Eureka Dashboard | http://localhost:8761 |
| API Gateway | http://localhost:8080 |
| Swagger API Docs | http://localhost:8080/swagger-ui.html |

## Common Commands

### Build Specific Service
```bash
cd service-name
mvn clean install
```

### Run Tests
```bash
mvn test
```

### Stop Docker Containers
```bash
docker-compose down
```

### View Logs
```bash
docker-compose logs -f service-name
```

### Build Docker Images
```bash
docker-compose build
```

## Troubleshooting

**Services not registering with Eureka:**
- Check if Eureka Server is running on port 8761
- Verify network connectivity

**Database connection errors:**
- Ensure PostgreSQL is running: `docker-compose ps`
- Check DB credentials in application.properties

## Next Steps

1. Review README.md for detailed architecture
2. Implement business logic in each service
3. Add Swagger annotations for API documentation
4. Create unit and integration tests
5. Set up CI/CD pipeline
6. Deploy to cloud (Azure/AWS)

## Need Help?

- Check logs: `docker-compose logs -f service-name`
- Review application.properties configuration
- Check Eureka dashboard for service health
- Verify all services are properly registered

---

**Happy coding! 🚀**
