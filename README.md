# 🎓 Learning Management System (LMS)

A full-stack, production-style **Learning Management System** built with **Java 17, Spring Boot, REST APIs, Spring Security, JWT, Spring Data JPA, Hibernate, MySQL, HTML, CSS, and JavaScript**.

The platform supports multiple user roles, course management, lessons with embedded learning videos, student enrollment, lesson completion, automatic progress tracking, dashboards, API testing, Docker deployment, and integration testing.

---

## 🚀 Key Features

### 🔐 Authentication & Authorization
- JWT-based authentication
- Student registration and login
- Role-based access control
- **Admin / Instructor / Student** roles
- Protected REST APIs
- Instructor ownership checks for course operations

### 📚 Course Management
- Course catalog
- Search courses by title/category
- Course creation
- Course update
- Course deletion
- Course details and lesson listing

### 🎥 Lesson & Video Management
- Multiple lessons per course
- Lesson ordering
- Lesson content
- Embedded YouTube learning videos
- Instructor/Admin lesson creation
- Student lesson completion

### 📈 Progress Tracking
- Track completed lessons
- Automatic course progress percentage
- Course-level student progress
- Enrollment-aware progress management

### 📊 Dashboard
- Dashboard statistics
- Current-user profile endpoint
- Course and enrollment insights
- Public application health check

### 🛡️ Reliability & Error Handling
- Bean/API validation
- Centralized exception handling
- Clear HTTP error responses
- Authentication and authorization handling

### 🧪 Testing
- Spring Boot integration tests
- H2 in-memory database for tests
- MySQL is not required for the test suite
- Frontend JavaScript syntax verification
- PowerShell verification script

### 🐳 Deployment Support
- Dockerfile
- Docker Compose
- MySQL container support
- Environment-variable based configuration

### 🔌 API Testing
- Postman collection included
- REST API endpoints organized by module

---

# 🎯 Learning Tracks

The LMS comes with four seeded learning tracks.

## ☕ Java Programming Masterclass

5 lessons covering:

- Java Fundamentals
- Control Flow and Arrays
- Collections and HashMaps
- Object-Oriented Programming
- Exceptions and Clean Java Code

## 🐍 Python Programming Masterclass

5 lessons covering:

- Python Basics
- Conditions, Loops and Functions
- Lists, Tuples, Sets and Dictionaries
- Files, Modules and Error Handling
- Object-Oriented Python

## 🗄️ MySQL Database Masterclass

5 lessons covering:

- Database Fundamentals
- SELECT, INSERT, UPDATE and DELETE
- Joins, GROUP BY and Subqueries
- Normalization and Database Design
- Indexes and Production SQL

## 🌱 Spring Boot Backend Engineering

5 lessons covering:

- Spring Boot Fundamentals
- REST API Development
- Spring Data JPA and MySQL
- Validation and Exception Handling
- Spring Security and JWT

Each course contains structured lessons and embedded educational videos.

---

# 🏗️ System Architecture

```text
                    ┌──────────────────────┐
                    │   HTML / CSS / JS    │
                    │    Web Frontend      │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │    REST Controllers  │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │ Spring Security +    │
                    │       JWT            │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │    Service Layer     │
                    │ Business Logic       │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │ Spring Data JPA /    │
                    │     Hibernate        │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │       MySQL 8        │
                    └──────────────────────┘
