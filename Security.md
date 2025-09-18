# 🔐 Security Architecture (VG-Level)

This document outlines the security measures, design decisions, and risk analysis for the Library Management System. It is written to satisfy the VG-level criteria for secure backend development.

---

## Overview

This project implements a **stateless JWT-based authentication system** with advanced protection against common attacks. Security is enforced at multiple levels:

* Request-level (filters, headers, CORS)
* Endpoint-level (`@PreAuthorize`, RBAC)
* Object-level (custom service-layer validation)
* Exception handling (centralized, consistent responses)

---

## Authentication & Authorization

### JWT-Based Auth

* **Access Token**: 1 hour expiration
* **Refresh Token**: 7 day expiration, stored in DB
* **Stateless Security**: No sessions, no cookies

### Login Flow

* Credentials are verified
* On success: access + refresh token are issued
* On failure: failed attempt recorded (see below)

### Role-Based Access Control

* `@PreAuthorize` used to restrict access
* Roles assigned via `UserRole` join table
* Service layer includes ownership checks (e.g. only the owner or an admin can access their loans)

### Custom Access Checks

* `UserAccessValidator` provides fine-grained control
* Ensures users cannot act on others' resources without permission

---

## Defensive Features

### Account Lockout

* 5 failed login attempts → locked for 15 minutes
* Lock status stored in DB (`lockedUntil` field)

### Rate Limiting

* Servlet filter (`RateLimitingFilter`) limits requests per IP
* Customizable limit and time window

### Secure Password Storage

* BCrypt used for hashing user passwords
* Salt included automatically

### Security Headers

* **CSP**: Content-Security-Policy
* **HSTS**: Strict-Transport-Security
* **X-Frame-Options**: DENY
* **X-Content-Type-Options**: nosniff
* **Referrer-Policy**: same-origin

### Exception Handling

* All custom exceptions handled centrally
* Spring exceptions (403, 404, 400, etc) mapped to clean JSON responses
* Validation errors return informative messages

---

## Risk Analysis & Limitations

| Threat                   | Mitigation                                             |
| ------------------------ | ------------------------------------------------------ |
| Brute Force              | Account lockout, rate limiting                         |
| Token Theft              | Short access token lifespan, refresh token revocation  |
| Unauthorized Data Access | Role checks + `UserAccessValidator`                    |
| SQL Injection            | JPA / Hibernate ORM used — no raw SQL                  |
| CSRF                     | Stateless JWT (no cookies) avoids this class of attack |
| XSS                      | CSP header enforced                                    |

### Remaining Risks

* Refresh tokens are long-lived. If stolen, they can be abused until expiry.
* No device/session management — user can't revoke individual refresh tokens.
* No email verification / 2FA (beyond assignment scope)

---

## Security vs UX Trade-offs

| Feature               | Impact                                                       |
| --------------------- | ------------------------------------------------------------ |
| Account lockout       | Improves security but may block legitimate users temporarily |
| Short token life (1h) | Requires token refresh more often                            |
| Rate limiting         | Can block automated tools (e.g. Swagger, Postman) if abused  |

---

## Design Rationale

* JWT chosen for statelessness and frontend compatibility
* Lockout + rate limiting required for VG criteria
* Token structure allows for scaling (e.g., distributed systems)
* SQLite used to simplify setup but can be swapped for production DB easily

---

## Summary

This backend system fulfills all technical security criteria outlined for VG level, including:

* Strong authentication and authorization model
* Protection against common attacks
* Secure error handling and audit logging

Only optional documentation and frontend-level considerations remain out of scope.

> Last reviewed: 2025-09-18