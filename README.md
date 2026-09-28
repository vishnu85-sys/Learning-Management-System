# LMS — Learning Management System

A production-style, end-to-end Learning Management System built with **Java 17, Spring Boot, REST APIs, Spring Security, JWT, Spring Data JPA, MySQL, HTML, CSS and JavaScript**.

## Highlights

- JWT authentication with **Admin / Instructor / Student** roles
- Student registration and login
- Course catalog with **search by title/category**
- Course CRUD with instructor ownership checks
- Lesson management with embedded **YouTube video lessons**
- Student enrollment and lesson completion
- Automatic course progress percentage
- Dashboard metrics
- Public health endpoint for monitoring
- Centralized validation and exception handling
- Responsive browser UI served directly by Spring Boot
- Postman collection for API testing
- H2-backed integration tests so the test suite does not require a live MySQL server

## Architecture

```text
HTML / CSS / JavaScript
          ↓
      REST Controllers
          ↓
Spring Security + JWT
          ↓
       Service Layer
          ↓
Spring Data JPA / Hibernate
          ↓
        MySQL 8
```

## Main APIs

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/auth/login` | Login and receive JWT |
| POST | `/api/auth/register` | Register a Student |
| GET | `/api/health` | Public application health check |
| GET | `/api/me` | Current authenticated user |
| GET | `/api/courses?query=java` | Search/list courses |
| POST | `/api/courses` | Create course (Admin/Instructor) |
| PUT | `/api/courses/{id}` | Update course |
| DELETE | `/api/courses/{id}` | Delete course |
| GET | `/api/courses/{id}/lessons` | List course lessons |
| POST | `/api/courses/{id}/lessons` | Create lesson |
| POST | `/api/enrollments/courses/{id}` | Enroll a student |
| GET | `/api/enrollments/me` | Student enrollments |
| POST | `/api/progress/lessons/{id}/complete` | Mark lesson complete |
| GET | `/api/progress/courses/{id}` | View course progress |
| GET | `/api/dashboard` | Dashboard statistics |

## Seeded Course Collection

The application seeds four core learning tracks, each with five lessons and embedded YouTube learning videos:

- **Java Programming Masterclass** — fundamentals, control flow, collections, OOP and exceptions
- **Python Programming Masterclass** — syntax, functions, collections, files/errors and OOP
- **MySQL Database Masterclass** — database concepts, CRUD SQL, joins/subqueries, normalization and indexes
- **Spring Boot Backend Engineering** — Spring Boot fundamentals, REST APIs, JPA/MySQL, validation and Spring Security/JWT

The seed is idempotent: restarting the application updates the demo lesson metadata without creating duplicate seeded courses.

## Demo Accounts

- **Admin:** `admin@lms.local` / `Admin@123`
- **Instructor:** `instructor@lms.local` / `Instructor@123`
- **Student:** `student@lms.local` / `Student@123`

Change demo credentials before production use.

## Run Locally

### 1. Create the database

Run `database/schema.sql` in MySQL Workbench.

### 2. Configure environment variables

PowerShell:

```powershell
$env:DB_PASSWORD="YOUR_MYSQL_PASSWORD"
$env:JWT_SECRET="replace-with-a-long-random-secret"
```

Optional:

```powershell
$env:DB_URL="jdbc:mysql://127.0.0.1:3306/lms_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
$env:DB_USER="root"
```

### 3. Start Spring Boot

```powershell
mvn spring-boot:run
```

### 4. Open the app

`http://localhost:8080`

### 5. Health check

`http://localhost:8080/api/health`

## Docker

Run the complete stack (Spring Boot + MySQL) with:

```powershell
docker compose up --build
```

Then open `http://localhost:8080`.

## Run Tests

The test suite uses an in-memory H2 database and does not require MySQL:

```powershell
mvn test
```

Or run the full verification script:

```powershell
.\scripts\verify.ps1
```

## Project Structure

```text
src/main/java/com/vishnu/lms
├── auth
├── config
├── controller
├── course
├── enrollment
├── exception
├── lesson
├── progress
├── security
├── service
└── user

src/main/resources
└── static
    ├── index.html
    ├── app.js
    └── styles.css

src/test
├── java/com/vishnu/lms/LmsApplicationTests.java
└── resources/application-test.properties

database/schema.sql
postman/LMS.postman_collection.json
```
