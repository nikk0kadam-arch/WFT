# Architecture Documentation

## System Architecture

### Microservices Pattern

This E-Commerce Backend uses a **microservices architecture** with the following principles:

1. **Single Responsibility**: Each service handles one business domain
2. **Loose Coupling**: Services communicate via REST APIs
3. **Independent Deployment**: Services can be deployed independently
4. **Fault Isolation**: Service failures don't cascade to others
5. **Scalability**: Services can be scaled independently

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                           External Clients                           │
│                    (Web, Mobile, Third-party APIs)                   │
└────────────────────────────────┬────────────────────────────────────┘
                                 │
                    ┌────────────▼────────────┐
                    │    API Gateway Port     │
                    │   (Spring Cloud        │
                    │    Gateway)            │
                    │   Port: 8080           │
                    └────────────┬────────────┘
                                 │
         ┌───────────────┬───────┼────────┬───────────────┐
         │               │       │        │               │
         ▼               ▼       ▼        ▼               ▼
    ┌─────────┐    ┌─────────┐  ┌──────────┐      ┌────────────┐
    │ User    │    │Product  │  │  Cart    │      │  Order     │
    │Service  │    │Catalog  │  │  Service │      │  Service   │
    │(8001)   │    │(8002)   │  │  (8003)  │      │  (8004)    │
    └─────────┘    └─────────┘  └──────────┘      └────────────┘
         │               │           │                   │
         │               │           │            ┌──────┼──────┐
         │               │           │            │      │      │
         │               │           │            ▼      ▼      ▼
         │               │           │       ┌─────────┐ ┌────────────┐
         │               │           │       │Payment  │ │Inventory   │
         │               │           │       │Service  │ │Service     │
         │               │           │       │(8005)   │ │(8006)      │
         │               │           │       └─────────┘ └────────────┘
         │               │           │            │           │
         │               └───────────┼────────────┴───────────┘
         │                           │                        │
         └───────────────────┬───────┴────────────────────────┘
                             │
                    ┌────────▼────────┐
                    │ Notification    │
                    │ Service (REST)  │
                    │ (8007)          │
                    └─────────────────┘
```

## Communication Patterns

### 1. **Synchronous Communication (Feign Client)**
- **Use Case**: Order Service calls Cart Service, Product Catalog Service
- **Advantage**: Strong consistency, immediate response
- **Disadvantage**: Tightly coupled, requires service availability
- **Examples**:
  - Order Service → Cart Service (fetch cart, clear cart)
  - Order Service → Product Service (fetch product details)
  - Cart Service → Product Service (fetch product pricing)

### 2. **Direct HTTP/REST**
- **Use Case**: API Gateway routes external requests to services
- **Pattern**: Load-balanced through Eureka service discovery
- **Ports**: 8001-8007 for individual services, 8080 for gateway

## Data Management

### Database Strategy: Database per Service

```
PostgreSQL (Single Instance for POC/Dev)
├── ecommerce database
│   ├── users table (User Service)
│   ├── products table (Product Catalog)
│   ├── carts table (Cart Service)
│   ├── orders table (Order Service)
│   ├── payments table (Payment Service)
│   └── inventory table (Inventory Service)
```

### Caching Strategy

- **Service-level**: User sessions, JWT tokens
- **Future Enhancement**: Implement Redis for high-traffic services (Product Catalog)

### Event Sourcing (Future Enhancement)

```
Order Aggregate:
- OrderCreated
- OrderConfirmed
- PaymentProcessed
- InventoryReserved
- InventoryReleased
- OrderShipped
- OrderDelivered
```

## Security Architecture

### Authentication Flow

```
1. Client
   └─→ POST /api/users/auth/login
       ├─ User: admin@example.com
       └─ Password: hashed

2. User Service
   ├─ Validate credentials
   ├─ Generate JWT token
   └─→ Return token

3. Client
   └─→ POST /api/orders with Authorization: Bearer <token>

4. API Gateway
   ├─ Validate JWT signature
   ├─ Check token expiration
   └─→ Route to Order Service

5. Order Service
   └─ Access user info from JWT claims
```

### JWT Structure

```
Header: {
  "alg": "HS256",
  "typ": "JWT"
}

Payload: {
  "sub": "user-123",
  "email": "user@example.com",
  "roles": ["CUSTOMER", "ADMIN"],
  "iat": 1234567890,
  "exp": 1234654290
}

Signature: HMACSHA256(header.payload, secret)
```

### Authorization (RBAC)

```
Roles:
├── ADMIN
│   ├── Can manage products
│   ├── Can view all orders
│   └── Can manage users
├── CUSTOMER
│   ├── Can view products
│   ├── Can manage own orders
│   └── Can manage own cart
└── GUEST
    └── Can view products only
```

## Service Responsibilities

### 1. Eureka Server
- Service registration and discovery
- Health monitoring
- Load balancing
- Automatic service deregistration on failure

### 2. Config Server
- Centralized configuration
- Environment-specific properties
- Runtime configuration refresh
- Placeholder support

### 3. API Gateway
- Request routing
- Load balancing
- Rate limiting
- Request/response modification
- JWT validation
- CORS handling

### 4. User Service
- User registration
- User authentication
- Profile management
- Role management
- JWT token generation

### 5. Product Catalog Service
- Product CRUD
- Category management
- Product search and filtering
- Inventory status


### 6. Cart Service
- Cart creation and management
- Add/remove/update items
- Cart validation
- Price calculation

### 7. Order Service
- Order creation
- Order status tracking
- Order validation
- Feign client calls to Cart and Product services

### 8. Payment Service
- Payment processing
- Dummy gateway integration
- Payment status tracking
- Transaction logging


### 9. Inventory Service
- Stock management
- Inventory updates
- Stock reservation
- Low-stock alerts


### 10. Notification Service
- Email notifications
- In-app notifications
- Notification status tracking

## Deployment Topology

### Single Host Deployment (Development)
```
Host Machine
├── Docker Container: PostgreSQL:5432
├── Process: Eureka Server:8761
├── Process: Config Server:8888
├── Process: API Gateway:8080
├── Process: User Service:8001
├── Process: Product Service:8002
├── Process: Cart Service:8003
├── Process: Order Service:8004
├── Process: Payment Service:8005
├── Process: Inventory Service:8006
└── Process: Notification Service:8007
```

### Multi-Host Deployment (Production - Azure)

```
Azure Resources:
├── Azure Container Registry: Store images
├── Azure Container Instances/AKS: Run services
├── Azure Database for PostgreSQL: Database
├── Application Insights: Monitoring
└── Azure Key Vault: Secrets management
```

## Performance Considerations

### Caching Strategy
1. **User Sessions**: In-memory cache
2. **API Response**: No caching (business requirements dependent)

### Connection Pooling
- PostgreSQL: HikariCP, min: 5, max: 20

### Async Processing
- Non-blocking database queries
- CompletableFuture for parallel operations

### Scalability
- Stateless services (scale horizontally)
- Database read replicas (future)
- Kafka partitioning for parallel processing
- Load balancing via Spring Cloud

## Monitoring & Observability

### Spring Boot Actuator Endpoints
```
GET /actuator/health              - Health check
GET /actuator/metrics             - Metrics
GET /actuator/prometheus          - Prometheus format
GET /actuator/threaddump         - Thread analysis
GET /actuator/heapdump           - Memory dump
```

### Logging Strategy
- Level: INFO (default), DEBUG (development)
- Format: JSON for ELK stack compatibility
- Centralized logging (future: ELK, Splunk)

### Tracing (Future Enhancement)
- Distributed tracing with Spring Cloud Sleuth
- Zipkin for visualization
- Correlation IDs across services

## Disaster Recovery

### Backup Strategy
- Daily PostgreSQL backups
- Redis persistence enabled
- Configuration stored in Git

### Failover Strategy
- Eureka-based service discovery
- Load balancing across instances
- Health checks and auto-recovery

## API Versioning

```
/api/v1/products              # Current version
/api/v2/products              # Future version
```

Implemented via API Gateway routing rules.

---

**Architecture Review Checklist:**
- [x] Microservices pattern defined
- [x] Communication patterns selected
- [x] Data management strategy
- [x] Security architecture
- [ ] Monitoring & observability setup
- [ ] Deployment topology
- [ ] Disaster recovery plan
- [ ] Performance optimization
