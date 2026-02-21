# BlinkDeliver - Cloud Native Order Management System

## Overview
BlinkDeliver is a comprehensive cloud-native e-commerce and order management platform built with Spring Boot 3.3.0. It provides a complete solution for user authentication, product catalog management, shopping cart, order processing, and payment gateway integration using Razorpay.

## Key Features
- **JWT-based Authentication** - Secure token-based authentication with custom JWT providers
- **Razorpay Payment Integration** - Complete payment processing with order verification and refunds
- **E-commerce Functionality** - Product catalog, categories, brands, shopping cart, and wishlist
- **Order Management** - Complete order lifecycle management with multiple statuses
- **Address Management** - User address management for delivery
- **Spring Cloud Integration** - Eureka service discovery, circuit breaker, and OpenFeign for microservices
- **Global Exception Handling** - Centralized error handling with custom exceptions
- **Comprehensive Validation** - Bean validation with custom validation rules
- **Monitoring & Metrics** - Spring Boot Actuator with Prometheus metrics
- **Code Quality** - JaCoCo test coverage reporting

## Technology Stack
- **Framework**: Spring Boot 3.3.0
- **Language**: Java 17
- **Build Tool**: Maven
- **Database**: MySQL
- **Authentication**: JWT (JJWT 0.13.0)
- **Payment Gateway**: Razorpay
- **Cloud**: Spring Cloud (2023.0.2)
  - Eureka Server & Client
  - Circuit Breaker (Resilience4j)
  - OpenFeign
- **Monitoring**: Spring Boot Actuator + Prometheus
- **Utilities**: Lombok
- **Testing**: JUnit 5, Spring Security Test, JaCoCo

## Project Structure
```
BlinkDeliver/
├── src/main/java/com/saurav/BlinkDeliver/
│   ├── BlinkDeliverApplication.java
│   ├── authenticationProviders/
│   │   └── JWTAuthenticationProvider.java
│   ├── config/
│   │   ├── CustomAuthenticationEntryPoint.java
│   │   ├── RazorpayConfig.java
│   │   └── SecurityConfig.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── AddressController.java
│   │   ├── BrandController.java
│   │   ├── CartController.java
│   │   ├── CategoryController.java
│   │   ├── OrderController.java
│   │   ├── PaymentController.java
│   │   ├── ProductController.java
│   │   └── WishlistController.java
│   ├── dto/
│   │   ├── UserDto.java
│   │   ├── RegisterDto.java
│   │   ├── LoginDto.java
│   │   ├── ProductDto.java
│   │   ├── CartDto.java
│   │   ├── CartItemDto.java
│   │   ├── OrderDto.java
│   │   ├── OrderItemDto.java
│   │   ├── OrderRequest.java
│   │   ├── AddressDto.java
│   │   ├── BrandDto.java
│   │   ├── CategoryDto.java
│   │   ├── PaymentOrderRequestDto.java
│   │   ├── PaymentOrderResponseDto.java
│   │   ├── PaymentVerificationDto.java
│   │   ├── PaymentRefundDto.java
│   │   ├── RazorpayOrderRequestDto.java
│   │   ├── RazorPayCreateOrderDto.java
│   │   ├── RazorpayVerifyPaymentDto.java
│   │   └── ErrorResponse.java
│   ├── entity/
│   │   ├── User.java
│   │   ├── Product.java
│   │   ├── Cart.java
│   │   ├── CartItem.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── Payment.java
│   │   ├── Address.java
│   │   ├── Brand.java
│   │   └── Category.java
│   ├── enums/
│   │   ├── Role.java
│   │   ├── OrderStatus.java
│   │   ├── PaymentMethod.java
│   │   ├── PaymentStatus.java
│   │   ├── CartType.java
│   │   └── SellingUnit.java
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── BadRequestException.java
│   │   ├── ResourceNotFoundException.java
│   │   └── UnauthorizedException.java
│   ├── filters/
│   │   ├── JWTAuthenticationFilter.java
│   │   ├── JWTRefreshFilter.java
│   │   └── JwtValidationFilter.java
│   ├── mapper/
│   │   └── AddressMapper.java
│   ├── repository/
│   │   └── [JPA Repositories for entities]
│   ├── service/
│   │   ├── UserService.java
│   │   ├── PaymentService.java
│   │   └── [Other business logic services]
│   ├── token/
│   │   └── [JWT token utilities]
│   └── utils/
│       └── JWTUtil.java
├── src/main/resources/
│   ├── application.properties
│   ├── data.sql
│   └── logback.xml
└── src/test/java/
    └── BlinkDeliverApplicationTests.java
```

## API Endpoints

### Authentication (`/auth`)
- **POST** `/auth/register` - User registration
- **POST** `/auth/login` - User login with JWT token
- **POST** `/auth/refresh` - Refresh JWT token
- **GET** `/auth/check-username/{username}` - Check username availability
- **GET** `/auth/check-email/{email}` - Check email availability

### Products (`/product`)
- **GET** `/product` - Get all products (paginated)
- **GET** `/product/{id}` - Get product details
- **POST** `/product` - Create product (admin)
- **PUT** `/product/{id}` - Update product (admin)
- **DELETE** `/product/{id}` - Delete product (admin)

### Categories (`/category`)
- **GET** `/category` - Get all categories
- **GET** `/category/{id}` - Get category details
- **POST** `/category` - Create category (admin)
- **PUT** `/category/{id}` - Update category (admin)

### Brands (`/brand`)
- **GET** `/brand` - Get all brands
- **GET** `/brand/{id}` - Get brand details
- **POST** `/brand` - Create brand (admin)
- **PUT** `/brand/{id}` - Update brand (admin)

### Cart (`/cart`)
- **GET** `/cart` - Get user's cart
- **POST** `/cart/add` - Add item to cart
- **PUT** `/cart/update` - Update cart item quantity
- **DELETE** `/cart/remove/{itemId}` - Remove item from cart
- **DELETE** `/cart/clear` - Clear entire cart

### Wishlist (`/wishlist`)
- **GET** `/wishlist` - Get user's wishlist
- **POST** `/wishlist/add` - Add product to wishlist
- **DELETE** `/wishlist/remove/{productId}` - Remove from wishlist

### Orders (`/order`)
- **POST** `/order/place` - Place new order
- **GET** `/order` - Get user's orders
- **GET** `/order/{orderId}` - Get order details
- **PUT** `/order/{orderId}/status` - Update order status (admin)
- **DELETE** `/order/{orderId}` - Cancel order

### Addresses (`/address`)
- **GET** `/address` - Get user's addresses
- **POST** `/address` - Add new address
- **PUT** `/address/{addressId}` - Update address
- **DELETE** `/address/{addressId}` - Delete address

### Payments (`/payment`)
- **POST** `/payment/create` - Create payment order (async)
- **POST** `/payment/verify` - Verify payment signature
- **POST** `/payment/refund` - Initiate refund
- **GET** `/payment/key` - Get Razorpay API key
- **POST** `/payment/test/signature` - Generate test signature

## Security Features

### JWT Authentication
- Token-based stateless authentication
- Custom JWT authentication provider
- JWT validation filter
- Token refresh mechanism
- Configurable token expiration

### Password Security
- BCrypt password encoding with configurable strength
- Salted hash storage
- Secure password validation

### CORS Configuration
- Configurable cross-origin resource sharing
- Support for frontend-backend separation
- Secure HTTP method restrictions

### Authorization
- Role-based access control (RBAC)
- Public endpoints for registration
- Protected endpoints for authenticated users
- Admin-only endpoints for management operations

## Exception Handling

Global exception handler with custom exceptions:
- **BadRequestException** - Invalid request data
- **ResourceNotFoundException** - Resource not found
- **UnauthorizedException** - Unauthorized access
- **ValidationException** - Input validation errors
- Custom error response format with error codes and messages

## Database Configuration

### Create Database
```sql
CREATE DATABASE blink_deliver;
```

### Update `application.properties`
```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/blink_deliver
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT Configuration
jwt.secret=your_secret_key
jwt.expiration=86400000

# Razorpay Configuration
razorpay.key.id=your_razorpay_key_id
razorpay.key.secret=your_razorpay_secret_key

# Eureka Client
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
eureka.instance.hostname=localhost

# Actuator & Prometheus
management.endpoints.web.exposure.include=health,info,prometheus
management.metrics.export.prometheus.enabled=true
```

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Razorpay account (for payment integration)

### Build and Run
```bash
# Clean and build
mvn clean build

# Run the application
mvn spring-boot:run

# Run with custom profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Access the Application
- **API Base URL**: `http://localhost:8080`
- **Swagger/OpenAPI** (if configured): `http://localhost:8080/swagger-ui.html`
- **Actuator Health**: `http://localhost:8080/actuator/health`
- **Prometheus Metrics**: `http://localhost:8080/actuator/prometheus`

## Testing

Run unit tests with coverage:
```bash
# Run all tests
mvn test

# Run tests with coverage report
mvn clean test jacoco:report
# Report will be generated at: target/site/jacoco/index.html

# Run specific test class
mvn test -Dtest=BlinkDeliverApplicationTests
```

## Development Guidelines

### Code Structure
- **DTOs** - Used for API request/response contracts
- **Entities** - JPA entities mapped to database tables
- **Services** - Business logic layer with transaction management
- **Controllers** - REST endpoints and request handling
- **Repositories** - Data access layer using Spring Data JPA

### Validation
- Bean validation annotations on DTOs
- Custom validator implementations
- Global exception handling for validation errors

### Logging
- SLF4J with Logback configuration
- Appropriate log levels for different scenarios
- Structured logging for debugging

### Async Processing
- CompletableFuture for non-blocking operations
- Async payment processing
- Improved performance for I/O operations

## Performance & Monitoring

### Metrics Collection
- Spring Boot Actuator for health checks
- Prometheus metrics for monitoring
- JaCoCo for test coverage analysis

### Best Practices
- Database query optimization with JPA
- Circuit breaker pattern for resilience
- Connection pooling for database
- Proper resource management

## License
Proprietary - BlinkDeliver

## Support
For issues and support, contact the development team.
