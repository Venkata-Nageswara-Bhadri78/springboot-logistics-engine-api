# Logistic Engine Backend

A production-oriented Logistics / Order Management System backend built using:

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA (Hibernate)
- MySQL
- JWT Authentication
- Maven

---

# Project Goal

The goal of this project is to:

- Learn Java Backend Development
- Learn Spring Boot from scratch
- Learn Hibernate / JPA
- Learn Spring Security
- Learn JWT Authentication & Authorization
- Learn RBAC (Role Based Access Control)
- Build production-ready REST APIs
- Understand enterprise backend architecture

---

# Tech Stack

| Layer | Technology |
|---------|---------|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| Security | Spring Security |
| ORM | Hibernate (JPA) |
| Database | MySQL |
| Authentication | JWT |
| Build Tool | Maven |
| IDE | VS Code |

---

# Project Structure

```text
src/main/java/com/logistic/logistic_engine
│
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── enums
├── exception
├── repository
├── security
├── service
└── LogisticEngineApplication.java
```

---

# Request Flow

Typical API flow:

```text
Client Request
        ↓
Security Layer
        ↓
Controller
        ↓
DTO
        ↓
Service
        ↓
Repository
        ↓
Entity
        ↓
Database
        ↓
Response DTO
        ↓
Client Response
```

---

# Authentication Flow

```text
POST /auth/login
        ↓
AuthController
        ↓
AuthService
        ↓
UserRepository
        ↓
Password Verification
        ↓
JwtService
        ↓
JWT Generation
        ↓
Login Response
```

---

# Authorization Flow

```text
Client Request
        ↓
Authorization Header
(Bearer Token)
        ↓
JwtAuthenticationFilter
        ↓
Token Validation
        ↓
Security Context
        ↓
Controller
```

---

# Database Tables

## Users

Stores authentication and role information.

```text
users
```

Fields:

```text
id
name
email
password
role
is_active
created_at
updated_at
```

---

## Customers

Stores customer profile information.

```text
customers
```

Fields:

```text
id
user_id
address
phone
created_at
```

---

## Agents

Stores delivery agent profile information.

```text
agents
```

Fields:

```text
id
user_id
vehicle_number
phone
created_at
```

---

## Orders

Stores logistics orders.

```text
orders
```

Fields:

```text
id
customer_id
agent_id
pickup_address
delivery_address
package_weight
priority
status
failure_reason
created_at
updated_at
```

---

## Order History

Stores audit trail of order status changes.

```text
order_history
```

Fields:

```text
id
order_id
previous_status
new_status
changed_by
note
created_at
```

---

# Roles

```text
ADMIN
AGENT
CUSTOMER
```

---

# Order Status

```text
CREATED
ASSIGNED
IN_TRANSIT
DELIVERED
FAILED
RETURNED
COMPLETED
CANCELLED
```

---

# API Endpoints

## Health

```http
GET /health
```

---

## Authentication

### Register

```http
POST /auth/register
```

### Login

```http
POST /auth/login
```

---

# Current Progress

## Completed

- Spring Boot Setup
- Security Configuration
- Global Exception Handling
- API Response Wrapper
- JPA Entity Design
- Repository Layer
- Register API
- Login API
- JWT Generation
- JWT Validation Filter

## Upcoming

- Current Logged-in User
- RBAC
- Customer APIs
- Agent APIs
- Order APIs
- Order Assignment
- Order Tracking
- Order History APIs
- Pagination
- Sorting
- Search Filters
- Refresh Tokens
- Docker
- Unit Testing
- Production Deployment

---

# Learning Notes

Core Spring Backend Flow:

```text
Request
↓
Security
↓
Controller
↓
DTO
↓
Service
↓
Repository
↓
Entity
↓
Database
↓
Response
```