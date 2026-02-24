# Eureka Discovery Server

A secure, production-ready Eureka Discovery Server built with Spring Boot 3.2+ and Java 21.

## Features

- **Standalone Mode**: Registry fetching and self-registration disabled.
- **Security**: 
  - Dashboard protected with Basic Auth.
  - CSRF disabled for Eureka client registration endpoints.
- **Observability**: Actuator health and info endpoints exposed.
- **Production Ready**: Graceful shutdown, compression enabled.

## Prerequisites

- Java 21
- Maven 3.8+

## Configuration

The server runs on port `8761` by default.

Credentials can be configured via environment variables:
- `EUREKA_USERNAME` (default: `admin`)
- `EUREKA_PASSWORD` (default: `password`)

## Helper Commands

### Build

```bash
mvn clean package
```

### Run

```bash
java -jar target/discovery-server-0.0.1-SNAPSHOT.jar
```

## Access

- Dashboard: [http://localhost:8761](http://localhost:8761)
- Health Check: [http://localhost:8761/actuator/health](http://localhost:8761/actuator/health)
