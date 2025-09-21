# 🔐 Security Architecture (VG-Level)

This document outlines the **security measures, design decisions, and risk analysis** for the Library Management System.
It is written to satisfy the **VG-level criteria** for secure backend development.

---

## Overview

The project implements a **stateless JWT-based authentication system** with advanced protections against common web security threats. Security is enforced at multiple levels:

* **Request-level** → filters, headers, CORS, rate limiting
* **Endpoint-level** → `@PreAuthorize` annotations, RBAC
* **Object-level** → custom service-layer ownership validation
* **Data-level** → strong hashing for sensitive fields
* **Exception handling** → centralized, consistent JSON error responses

---

## Authentication & Authorization

### JWT-Based Authentication

* **Access Token**: 1 hour expiration
* **Refresh Token**: 7 day expiration, stored in DB
* **Stateless Security**: No sessions, no cookies

### Login Flow

* Credentials are verified against hashed values in DB
* On success → new access + refresh tokens are issued
* On failure → failed login attempt counter updated (lockout may apply)

### Role-Based Access Control (RBAC)

* `@PreAuthorize` annotations restrict access at the controller level
* Roles assigned via `UserRole` join table
* Service layer includes ownership checks (e.g., only the owner or an admin can access their loans)

### Custom Access Checks

* `UserAccessValidator` enforces fine-grained, per-resource authorization
* Prevents users from accessing or modifying other users’ resources

---

## Defensive Features

### Account Lockout

* **5 failed login attempts** → account locked for **15 minutes**
* Lock status tracked in DB (`lockedUntil` column)

### Rate Limiting

* Custom `RateLimitingFilter` limits requests per IP
* Configurable request limit + time window

### Secure Data Storage

* **Passwords** → stored using BCrypt with automatic per-user salt
* **National ID** → hashed before persistence (privacy protection)
* **Timestamps** → stored in SQLite-friendly format (`yyyy-MM-dd HH:mm:ss`)

### Security Headers

* **CSP**: `Content-Security-Policy`
* **HSTS**: `Strict-Transport-Security`
* **X-Frame-Options**: `DENY`
* **X-Content-Type-Options**: `nosniff`
* **Referrer-Policy**: `same-origin`

### Exception Handling

* Centralized handler for all custom exceptions
* Maps Spring security errors (403, 404, 400, etc.) to clean JSON responses
* Validation errors return **specific, user-friendly messages**

---

## Risk Analysis & Limitations

| Threat                   | Mitigation                                             |
| ------------------------ | ------------------------------------------------------ |
| Brute Force              | Account lockout, rate limiting                         |
| Token Theft              | Short access token lifespan, refresh token revocation  |
| Unauthorized Data Access | RBAC + `UserAccessValidator`                           |
| SQL Injection            | JPA / Hibernate ORM used — no raw SQL                  |
| CSRF                     | Stateless JWT (no cookies) avoids this class of attack |
| XSS                      | Strict CSP header                                      |

### Remaining Risks

* **Refresh tokens** are long-lived: if stolen, they remain valid until expiry.
* **No device/session management**: users cannot revoke a single refresh token.
* **No email verification / 2FA**: out of scope for current assignment.

---

## Security vs UX Trade-offs

| Feature               | Impact                                                       |
| --------------------- | ------------------------------------------------------------ |
| Account lockout       | Improves security but may block legitimate users temporarily |
| Short token life (1h) | Requires more frequent token refreshes                       |
| Rate limiting         | May affect automated tools (Swagger, Postman) if misused     |

---

## Design Rationale

* **JWT** chosen for statelessness and frontend compatibility
* **Lockout + rate limiting** implemented to meet VG criteria
* **Token model** supports scaling to distributed systems
* **Sensitive data hashing** ensures protection even if DB is leaked
* **SQLite** used for assignment portability — can be swapped for a production-ready DB

---

## Implementation Notes (Developer Reference)

| Component                | Location / Package | Responsibility                                 |
| ------------------------ | ------------------ | ---------------------------------------------- |
| `AuthService.refresh()`  | `services.auth`    | Validates refresh token, issues new JWTs       |
| `UserAccessValidator`    | `security`         | Ownership checks on service-layer operations   |
| `RateLimitingFilter`     | `filters`          | Per-IP request limiting                        |
| `GlobalExceptionHandler` | `exceptions`       | Centralized error handling, JSON error mapping |
| `User` entity            | `entities`         | Stores hashed password, hashed nationalId      |
| `SecurityConfig`         | `config`           | Configures Spring Security + JWT filter chain  |

---

## Summary

This backend system fulfills **all technical security criteria** required for VG level:

* Strong authentication and authorization model
* Secure handling of sensitive data (passwords + national IDs)
* Protection against brute force, injection, and token theft
* Hardened error handling and centralized logging

> Last reviewed: 2025-09-21