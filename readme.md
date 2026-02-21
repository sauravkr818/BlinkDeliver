# Spring Boot User Registration System

## Overview
A complete Spring Boot application implementing user registration functionality with BCrypt password encoding, validation, and REST API endpoints.

## Features
- User registration with validation
- BCrypt password encoding
- Duplicate username/email checking
- RESTful API endpoints
- MySQL database integration
- Spring Security configuration
- Comprehensive error handling

## Project Structure
```
BlinkDeliver/
├── src/main/java/com/saurav/BlinkDeliver/
│   ├── config/
│   │   └── SecurityConfig.java
│   ├── controller/
│   │   └── AuthController.java
│   ├── dto/
│   │   ├── RegisterDto.java
│   │   ├── UserDto.java
│   │   └── LoginDto.java
│   ├── entity/
│   │   └── User.java
│   ├── enums/
│   │   ├── Role.java
│   │   ├── OrderStatus.java
│   │   └── PaymentMethod.java
│   ├── mapper/
│   │   └── UserMapper.java
│   ├── repository/
│   │   └── UserRepository.java
│   ├── service/
│   │   └── UserService.java
│   └── BlinkDeliverApplication.java
└── src/main/resources/
    └── application.properties
```

## API Endpoints

### User Registration
- **POST** `/api/v1/auth/register`
- **Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "securePassword123",
  "role": "CUSTOMER"
}
```
- **Response (201 Created):**
```json
{
  "success": true,
  "message": "User registered successfully",
  "user": {
    "userId": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "role": "CUSTOMER",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
}
```

### Check Username Availability
- **GET** `/api/v1/auth/check-username/{username}`
- **Response:**
```json
{
  "available": true,
  "username": "john_doe"
}
```

### Check Email Availability
- **GET** `/api/v1/auth/check-email/{email}`
- **Response:**
```json
{
  "available": false,
  "email": "john@example.com"
}
```

## Dependencies
The following dependencies are included in `pom.xml`:
- `spring-boot-starter-web` - Web MVC support
- `spring-boot-starter-data-jpa` - JPA/Hibernate support
- `spring-boot-starter-validation` - Bean validation
- `spring-boot-starter-security` - Security framework
- `mysql-connector-j` - MySQL database driver
- `lombok` - Boilerplate code reduction

## Database Configuration
Update `application.properties` with your MySQL database details:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Running the Application
1. Ensure MySQL is running
2. Create database: `CREATE DATABASE BlinkDeliver_db;`
3. Run the application: `mvn spring-boot:run`
4. Access endpoints at: `http://localhost:8080/api/v1/auth/`

## Security Features
- BCrypt password encoding with strength 12
- CORS configuration for cross-origin requests
- Public endpoints for registration and authentication
- Protected endpoints for authenticated users

## Error Handling
- **409 Conflict** - Username or email already exists
- **400 Bad Request** - Validation errors
- **500 Internal Server Error** - Unexpected errors

## Validation Rules
- Username: 3-50 characters, required
- Email: Valid email format, required
- Password: Required (encoded with BCrypt)
- Role: Required (defaults to CUSTOMER)
