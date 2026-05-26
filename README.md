# ReturnRadar - Reverse Logistics Analytics System

## Problem Statement

E-commerce and fashion brands lose millions annually to fraudulent and high-volume product returns. ReturnRadar provides real-time analytics to detect high-return products, size-fit issues, suspicious customer behavior, and operational trends.

## Tech Stack

- Java 25
- Spring Boot 4.0.6
- Spring Data JPA
- PostgreSQL
- Maven
- Docker and Docker Compose
- Swagger/OpenAPI with Springdoc
- JUnit 5 and Mockito
- Lombok

## Architecture

ReturnRadar follows a layered Spring Boot architecture:

```text
Controller -> Service -> Repository -> Entity
```

- **Controller**: Exposes REST APIs and wraps responses in `ApiResponse<T>`.
- **Service**: Contains business rules, validation, analytics calculations, and transactional workflows.
- **Repository**: Uses Spring Data JPA for persistence and query methods.
- **Entity**: Models database tables with JPA annotations.
- **DTOs**: Separate request and response contracts from persistence entities.
- **Mappers**: Convert between entities and DTOs using plain Java static methods.
- **Exception Handling**: Centralized through `GlobalExceptionHandler`.
- **Scheduler**: Aggregates hourly analytics snapshots with Spring Scheduling.
- **Seed Data**: Loads sample customers, products, orders, returns, and inspections on first startup.

## Database Tables

The current project defines 8 JPA entities:

| Entity | Table | Key Fields |
| --- | --- | --- |
| `Customer` | `customers` | `customerId`, `name`, `email`, `city`, `totalOrders`, `totalReturns`, `totalRefundAmount`, `flaggedAsSuspicious` |
| `Product` | `products` | `productId`, `name`, `brand`, `category`, `size`, `price`, `totalSold`, `totalReturned`, `returnRate` |
| `Order` | `orders` | `orderId`, `customer`, `orderDate`, `deliveryDate`, `totalAmount`, `city`, `status` |
| `OrderItem` | `order_items` | `orderItemId`, `order`, `product`, `quantity`, `unitPrice`, `size`, `returnRequested` |
| `ReturnRequest` | `return_requests` | `returnId`, `orderId`, `productId`, `customerId`, `returnReason`, `quantity`, `returnStatus`, `requestedAt`, `refundAmount` |
| `ReturnInspection` | `return_inspections` | `inspectionId`, `returnRequest`, `warehouseId`, `productCondition`, `approved`, `inspectedAt`, `inspectorName` |
| `AnalyticsSnapshot` | `analytics_snapshots` | `snapshotId`, `snapshotTime`, `totalReturns`, `totalRefundAmount`, `highReturnProductCount`, `suspiciousCustomerCount`, `mostCommonReason` |
| `DataQualityIssue` | `data_quality_issues` | `issueId`, `issueType`, `referenceId`, `referenceType`, `description`, `detectedAt`, `resolved` |

## API Reference

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/customers` | Create a customer |
| `GET` | `/api/customers` | List all customers |
| `GET` | `/api/customers/{id}` | Get customer by ID |
| `POST` | `/api/products` | Create a product |
| `GET` | `/api/products` | List all products |
| `GET` | `/api/products/{id}` | Get product by ID |
| `POST` | `/api/orders` | Create an order with items |
| `GET` | `/api/orders/{id}` | Get order by ID |
| `GET` | `/api/orders/customer/{customerId}` | List orders for a customer |
| `POST` | `/api/returns` | Create a return request |
| `GET` | `/api/returns` | List all return requests |
| `GET` | `/api/returns/{id}` | Get return request by ID |
| `PATCH` | `/api/returns/{id}/status?status={status}` | Update return status |
| `POST` | `/api/inspections` | Create a return inspection |
| `GET` | `/api/inspections/return/{returnId}` | Get inspection by return ID |
| `GET` | `/api/analytics/high-return-products` | Get products with return rate above 10% |
| `GET` | `/api/analytics/return-reasons` | Get return reason distribution |
| `GET` | `/api/analytics/size-issue-products` | Get products with size-related return issues |
| `GET` | `/api/analytics/location-wise-returns` | Get return totals grouped by city |
| `GET` | `/api/analytics/suspicious-customers` | Get suspicious customers |
| `GET` | `/api/analytics/data-quality-issues` | Get unresolved data quality issues |
| `GET` | `/api/analytics/snapshot` | Get latest analytics snapshot |

## How to Run Locally

1. Install prerequisites:
   - Java 25
   - Maven
   - PostgreSQL
2. Create the database:

```sql
CREATE DATABASE returnradar_db;
```

3. Confirm local database settings in `src/main/resources/application-local.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/returnradar_db
    username: postgres
    password: postgres
```

4. Run the application:

```bash
mvn spring-boot:run
```

5. Access Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

## How to Run with Docker

1. Build the jar:

```bash
mvn package -DskipTests
```

2. Start PostgreSQL and the app:

```bash
docker-compose up --build
```

3. Access Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

## Sample API Requests

### Create Customer

```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Rahul Sharma",
    "email": "rahul.new@email.com",
    "phone": "9876543210",
    "city": "Bangalore"
  }'
```

### Create Product

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nike Air Max",
    "brand": "Nike",
    "category": "SHOES",
    "size": "9",
    "color": "Black",
    "price": 4999,
    "stockQuantity": 100
  }'
```

### Create Return

```bash
curl -X POST http://localhost:8080/api/returns \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": 1,
    "productId": 1,
    "customerId": 1,
    "reason": "SIZE_TOO_SMALL",
    "description": "Shoe size was too small",
    "quantity": 1,
    "city": "Bangalore",
    "refundAmount": 4999
  }'
```

### Get High-Return Products

```bash
curl http://localhost:8080/api/analytics/high-return-products
```

### Get Suspicious Customers

```bash
curl http://localhost:8080/api/analytics/suspicious-customers
```

## Future Improvements

- ML-based fraud detection
- Real-time return tracking dashboard
- Kafka integration for event streaming
- Role-based access control
- Email notification on suspicious detection
