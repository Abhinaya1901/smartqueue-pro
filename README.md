# SmartQueue Pro
### Intelligent Ticket Routing & SLA Management System

A production-grade service desk ticketing system built with Java Spring Boot, featuring an automated routing engine, real-time SLA monitoring, and JWT authentication.

## Tech Stack
- Java 21, Spring Boot 3.3, Spring Security 6
- Spring Data JPA + MySQL 8
- JWT Authentication
- Caffeine Cache
- Swagger/OpenAPI 3
- Docker

## Key Features
- **Smart Routing Engine** — Strategy Pattern with 3 algorithms: Load-Balanced, Skill-Based, Priority-Based
- **SLA Monitor** — Multithreaded background engine using ScheduledExecutorService
- **Event-Driven** — Spring ApplicationEventPublisher for ticket lifecycle events
- **Role-Based Access** — EMPLOYEE / AGENT / MANAGER / ADMIN
- **Dashboard UI** — Dark-themed real-time analytics dashboard

## Run Locally
```bash
docker run --name smartqueue-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=smartqueue -p 3306:3306 -d mysql:8
./mvnw spring-boot:run
```

Open http://localhost:8080 for the dashboard.
API docs at http://localhost:8080/swagger-ui/index.html

## Architecture
Ticket created → Routing Engine selects agent (Strategy Pattern) → SLA Monitor watches deadline → Auto-escalates on breach → Event published → Async notification sent
