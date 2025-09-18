# 📚 Library Management System

A secure and extensible REST API for managing a library system — **built with modern Spring Boot practices**, hardened for production, and targeting VG-level criteria.

---

## Tech Stack

| Layer          | Technology                                  |
| -------------- | ------------------------------------------- |
| **Language**   | Java 21                                     |
| **Framework**  | Spring Boot 3.4.5                           |
| **Build Tool** | Maven                                       |
| **Database**   | SQLite (via JDBC, JPA portable config)      |
| **ORM**        | Spring Data JPA (Hibernate)                 |
| **REST API**   | Spring Web                                  |
| **Security**   | Spring Security + JWT (access/refresh)      |
| **Auth**       | Stateless auth w/ Role & Method-based ACL   |
| **Testing**    | Spring Boot Starter Test (JUnit 5, MockMvc) |
| **Logging**    | SLF4J + Logback                             |
| **Validation** | Jakarta Bean Validation                     |
| **Dev Tools**  | Spring DevTools, Lombok                     |

---

## Features

### Core Functionality

* Full CRUD for:
    * Books
    * Authors
    * Users
    * Loans
* Loan return logic (with available copies management)
* Search books by title and author
* Pagination & sorting (where applicable)
* DTO-based input/output validation

### Security Architecture

* **Stateless JWT Auth**

    * Access token: 1h
    * Refresh token: 7d
* **Login & Register endpoints** (public)
* **Logout & Refresh token support**
* **Role-based Access Control (RBAC)** using `@PreAuthorize`
* **Per-user data protection** via service-layer checks

### VG-Level Security Hardening

* **Account lockout** after 5 failed attempts (15 min lock)
* **Rate limiting** (via servlet filter)
* **Security headers**: CSP, HSTS, X-Frame-Options, etc.
* **Exception handling**: custom + Spring exceptions covered
* **Audit logging** for login, logout, refresh, and register
* **Password hashing** using BCrypt
* **Input validation** via DTOs and JSR-380
* **CORS policy**: secure defaults
* **Method-level security**: `@PreAuthorize`, `UserAccessValidator`

---

## Testing

* Unit tests for services
* Integration tests for controllers
* Custom `@WithMockUser` configuration for RBAC scenarios
* Separate in-memory DB used for test isolation

---

## Running the Application

1. Clone this repository:

   ```bash
   git clone https://github.com/adhSwede/library-api.git
   cd library-api
   ```

2. Build and run:

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. Default port: `http://localhost:8080`

---

## Project Structure

```bash
src/
├── main/
│   ├── java/dev/jonas/library/
│   │   ├── controllers/
│   │   ├── services/
│   │   ├── entities/
│   │   ├── dtos/
│   │   ├── security/
│   │   ├── mappers/
│   │   ├── repositories/
│   │   └── exceptions/
│   └── resources/
│       └── application.properties
├── test/
│   └── java/dev/jonas/library/
│       └── (unit + integration tests)
```

---

## Authentication Example

### Register

```http
POST /auth/register
Content-Type: application/json

{
  "email": "newuser@example.com",
  "firstName": "Jonas",
  "lastName": "Tester",
  "password": "Secure123!"
}
```

### Login

```http
POST /auth/login
Content-Type: application/json

{
  "email": "newuser@example.com",
  "password": "Secure123!"
}
```

Returns:

```json
{
  "expiresAt": "2025-09-18T16:32:04",
  "accessToken": "eyJhbGciOiJIUzI1...",
  "refreshToken": "eyJhbGciOiJIUzI1..."
}
```

Use `Authorization: Bearer <accessToken>` for all protected endpoints.

---

## Notes

* SQLite is used for portability during development; easily swappable for PostgreSQL or MariaDB in production.
* Written for educational/demo purposes — includes optional VG-level features from course specification.

---

## Author

* **Jonas (adhSwede)** — Fullstack Developer Student