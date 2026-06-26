# Multi-stage Dockerfile for E-Commerce Backend Services

# Stage 1: Build
FROM maven:3.9.4-eclipse-temurin-17 as builder

WORKDIR /build

# Copy parent POM
COPY pom.xml .

# Copy all service modules
COPY eureka-server eureka-server
COPY config-server config-server
COPY api-gateway api-gateway
COPY user-service user-service
COPY product-catalog-service product-catalog-service
COPY cart-service cart-service
COPY order-service order-service
COPY payment-service payment-service
COPY inventory-service inventory-service
COPY notification-service notification-service
COPY shared-library shared-library

# Build all modules
RUN mvn clean install -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy built JAR from builder
COPY --from=builder /build/*/target/*.jar ./

# Create non-root user
RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser

USER appuser

EXPOSE 8001 8002 8003 8004 8005 8006 8007 8761 8888 8080

CMD ["java", "-jar", "*.jar"]
