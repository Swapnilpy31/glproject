# Secure REST API Application

A production-ready Spring Boot application using Spring Security 6 and JWT authentication.

## Features
- **Java 21 & Spring Boot 3.2+**
- **JWT Authentication**: Stateless, token-based security.
- **RBAC**: Role-based access control (User/Admin).
- **Secure Headers**: X-Frame-Options, CSP enabled.
- **Actuator**: Hardened health and info endpoints.
- **Graceful Shutdown**: Enabled.

## Build and Run

### Build
```bash
mvn clean package
```

### Run
```bash
java -jar target/config-server-0.0.1-SNAPSHOT.jar
```

## Usage

### 1. Login (Get Token)
**POST** `/auth/login`
```json
{
    "username": "admin",
    "password": "password"
}
```
Response:
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 2. Access Protected Endpoint
**GET** `/api/demo`
Header: `Authorization: Bearer <token>`

### 3. Access Admin Endpoint
**GET** `/api/demo/admin`
Header: `Authorization: Bearer <token>`
(Requires ADMIN role)

## Users (InMemory)
- **User**: `user` / `password` (Role: USER)
- **Admin**: `admin` / `password` (Role: ADMIN)
