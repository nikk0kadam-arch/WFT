# Project Structure Overview

## Directory Layout

```
ecommerce-backend/
├── pom.xml                          # Parent POM with dependencies
├── docker-compose.yml               # Docker Compose for infrastructure
├── Dockerfile                       # Multi-stage Docker build
├── .env                             # Environment variables
├── .gitignore                       # Git ignore rules
├── README.md                        # Comprehensive documentation
├── QUICKSTART.md                    # Quick start guide
├── ARCHITECTURE.md                  # Architecture details
│
├── config/                          # Configuration files
│   └── init-db.sql                  # PostgreSQL initialization
│
├── shared-library/                  # Shared components
│   ├── pom.xml
│   └── src/main/java/com/ecommerce/shared/
│       └── dto/
│           └── ApiResponse.java     # Standard API response
│
├── eureka-server/                   # Service Discovery
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/ecommerce/eureka/
│       │   └── EurekaServerApplication.java
│       └── main/resources/
│           └── application.yml
│
├── config-server/                   # Configuration Management
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/ecommerce/config/
│       │   └── ConfigServerApplication.java
│       └── main/resources/
│           └── application.yml
│
├── api-gateway/                     # API Gateway
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/ecommerce/gateway/
│       │   └── ApiGatewayApplication.java
│       └── main/resources/
│           └── application.yml
│
├── user-service/                    # User Management
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/ecommerce/user/
│       │   ├── entity/              # (To be created)
│       │   ├── repository/          # (To be created)
│       │   ├── service/             # (To be created)
│       │   ├── controller/          # (To be created)
│       │   └── UserServiceApplication.java
│       └── main/resources/
│           └── application.yml
│
├── product-catalog-service/         # Product Management
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/ecommerce/catalog/
│       │   ├── entity/              # (To be created)
│       │   ├── repository/          # (To be created)
│       │   ├── service/             # (To be created)
│       │   ├── controller/          # (To be created)
│       │   └── ProductCatalogServiceApplication.java
│       └── main/resources/
│           └── application.yml
│
├── cart-service/                    # Shopping Cart
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/ecommerce/cart/
│       │   └── CartServiceApplication.java
│       └── main/resources/
│           └── application.yml
│
├── order-service/                   # Order Processing
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/ecommerce/order/
│       │   └── OrderServiceApplication.java
│       └── main/resources/
│           └── application.yml
│
├── payment-service/                 # Payment Processing
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/ecommerce/payment/
│       │   └── PaymentServiceApplication.java
│       └── main/resources/
│           └── application.yml
│
├── inventory-service/               # Inventory Management
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/ecommerce/inventory/
│       │   └── InventoryServiceApplication.java
│       └── main/resources/
│           └── application.yml
│
└── notification-service/            # Notifications & Events
    ├── pom.xml
    └── src/
        ├── main/java/com/ecommerce/notification/
        │   └── NotificationServiceApplication.java
        └── main/resources/
            └── application.yml
```

## Service Ports & Roles

| Service | Port | Role |
|---------|------|------|
| Eureka Server | 8761 | Service Discovery & Registration |
| Config Server | 8888 | Centralized Configuration |
| API Gateway | 8080 | Request Routing & Load Balancing |
| User Service | 8001 | Authentication, Registration, Profile |
| Product Catalog | 8002 | Products, Categories, Inventory Status |
| Cart Service | 8003 | Shopping Cart Management |
| Order Service | 8004 | Order Processing & Tracking |
| Payment Service | 8005 | Payment Processing |
| Inventory Service | 8006 | Stock Management |
| Notification Service | 8007 | Event-Driven Notifications |

## Dependencies Between Services

```
API Gateway
    ├── routes to → User Service
    ├── routes to → Product Catalog Service
    ├── routes to → Cart Service
    ├── routes to → Order Service
    ├── routes to → Payment Service
    ├── routes to → Inventory Service
    └── routes to → Notification Service

Order Service
    ├── calls → Cart Service (via Feign)
    ├── calls → Product Catalog Service (via Feign)
    └── calls → Payment Service (via Feign)

Inventory Service
    └── calls → Order Service (via REST/Feign)

Payment Service
    └── calls → Notification Service (via REST/Feign)

Notification Service
    └── sends notifications to users
```

## Infrastructure Components

### PostgreSQL
- **Port**: 5432
- **Database**: ecommerce
- **Stores**: All service data
- **Tables**: users, products, categories, carts, orders, payments, inventory, notifications

## Next Steps to Complete Implementation

### 1. **User Service** - Entity & Repository Layer
```
├── entity/User.java
├── entity/Role.java
├── repository/UserRepository.java
├── repository/RoleRepository.java
├── service/UserService.java
├── service/AuthenticationService.java
├── controller/AuthController.java
└── security/JwtTokenProvider.java
```

### 2. **Product Catalog Service** - Database Layer
```
├── entity/Product.java
├── entity/Category.java
├── repository/ProductRepository.java
├── repository/CategoryRepository.java
├── service/ProductService.java
├── controller/ProductController.java
└── controller/CategoryController.java
```

### 3. **Order Service** - Business Logic
```
├── entity/Order.java
├── entity/OrderItem.java
├── repository/OrderRepository.java
├── service/OrderService.java
├── client/CartServiceClient.java
├── client/InventoryServiceClient.java
├── event/OrderCreatedEvent.java
├── event/OrderEventProducer.java
└── controller/OrderController.java
```

### 4. **Payment Service** - Payment Processing
```
├── entity/Payment.java
├── repository/PaymentRepository.java
├── service/PaymentService.java
├── service/PaymentGatewayService.java
├── event/PaymentEventListener.java
└── controller/PaymentController.java
```

### 5. **Cart Service** - Feign Clients
```
├── entity/Cart.java
├── entity/CartItem.java
├── repository/CartRepository.java
├── service/CartService.java
├── client/ProductServiceClient.java
└── controller/CartController.java
```

### 6. **Inventory Service** - Stock Management
```
├── entity/Inventory.java
├── entity/InventoryTransaction.java
├── repository/InventoryRepository.java
├── service/InventoryService.java
└── controller/InventoryController.java
```

### 7. **Notification Service** - Email Notifications
```
├── entity/Notification.java
├── repository/NotificationRepository.java
├── service/NotificationService.java
├── service/EmailService.java
└── controller/NotificationController.java
```
└── controller/NotificationController.java
```

## Testing Structure (To Be Added)

Each service should have:
```
src/test/java/com/ecommerce/[service]/
├── entity/[Entity]Test.java
├── service/[Service]Test.java
├── controller/[Controller]Test.java
└── integration/[Integration]Test.java

src/test/resources/
└── application-test.yml
```

## Configuration Management

### Local Development (.env)
- Database: PostgreSQL on localhost:5432
- JWT Secret: development key (CHANGE IN PRODUCTION)

### Production (Config Server)
- Environment-specific properties
- Sensitive data in secrets manager
- Auto-refresh configuration

---

**Status: Project scaffold complete ✓**
- [x] Maven multi-module setup
- [x] Service applications created
- [x] Docker Compose infrastructure
- [x] Initial application.yml configurations
- [ ] Entity models and repositories
- [ ] Service business logic
- [ ] REST controllers
- [ ] Security & JWT implementation
- [ ] Unit and integration tests
