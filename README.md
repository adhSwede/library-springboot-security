# Library Management System

## Tech Stack

| Layer          | Technology                             |
|----------------|----------------------------------------|
| **Language**   | Java 21                                |
| **Framework**  | Spring Boot 3.4.5                      |
| **Build Tool** | Maven                                  |
| **Database**   | MariaDB                                |
| **ORM**        | Spring Data JPA (Hibernate)            |
| **REST API**   | Spring Web                             |
| **Dev Tools**  | Spring Boot DevTools, Lombok           |
| **Testing**    | Spring Boot Starter Test (JUnit, etc.) |

### Project Overview

This is a Library Management System built using **Spring Boot 3.4.5**, **MariaDB**, and **Java 21**. The project
provides a RESTful API to manage books, authors, loans, and users. It uses **Spring Data JPA (Hibernate)** for ORM and *
*Maven** for dependency management.

### Features

- **CRUD operations** for Authors, Books, and Users.
- **Loan management** allowing users to borrow and return books.
- Search functionality for filtering books by title and author.
- Robust validation on all inputs to ensure data integrity.

### Setup

1. Clone this repository.
2. Install dependencies:  
   `mvn clean install`
3. Set up MariaDB and configure your `application.properties` file with the correct database credentials.
4. Run the application:  
   `mvn spring-boot:run`