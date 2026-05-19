# Team Task Manager — Backend API

Spring Boot REST API with JWT authentication, MySQL, and per-project RBAC (`ADMIN` / `MEMBER`).

## Prerequisites

- Java 17+
- Maven 3.9+
- MySQL 8 with database `team_task_db`

## Configuration

`src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/team_task_db?...
spring.datasource.username=devesh
spring.datasource.password=devesh77
server.port=8080
```

## Run

```bash
cd backend
mvn spring-boot:run
```

Base URL: `http://localhost:8080`

## Authentication

All endpoints except `/api/auth/**` require header:

```
Authorization: Bearer <token>
```

### Signup

`POST /api/auth/signup`

```json
{
  "name": "Devesh",
  "email": "devesh@example.com",
  "password": "devesh77"
}
```

**Response (201):**

```json
{
  "id": 1,
  "name": "Devesh",
  "email": "devesh@example.com",
  "token": "eyJhbGciOiJIUzI1NiIs..."
}
```

### Login

`POST /api/auth/login`

```json
{
  "email": "devesh@example.com",
  "password": "devesh77"
}
```

**Response (200):** same shape as signup.

---

## Projects

| Method | URL | Access |
|--------|-----|--------|
| POST | `/api/projects` | Authenticated user (creator becomes ADMIN) |
| GET | `/api/projects` | List projects you are a member of |
| GET | `/api/projects/{id}` | Project member |
| PUT | `/api/projects/{id}` | Project ADMIN |
| DELETE | `/api/projects/{id}` | Project ADMIN |

**Create project body:**

```json
{
  "name": "Website Redesign",
  "description": "Q2 launch"
}
```

---

## Project members

| Method | URL | Access |
|--------|-----|--------|
| POST | `/api/projects/{projectId}/members` | ADMIN |
| GET | `/api/projects/{projectId}/members` | Member |
| PUT | `/api/projects/{projectId}/members/{memberId}` | ADMIN |
| DELETE | `/api/projects/{projectId}/members/{memberId}` | ADMIN |

**Add member body:**

```json
{
  "email": "member@example.com",
  "role": "MEMBER"
}
```

Roles: `ADMIN`, `MEMBER`

**Update role body:**

```json
{
  "role": "ADMIN"
}
```

---

## Tasks

| Method | URL | Access |
|--------|-----|--------|
| POST | `/api/projects/{projectId}/tasks` | Member |
| GET | `/api/projects/{projectId}/tasks` | Member |
| GET | `/api/tasks/mine` | Assigned to current user |
| GET | `/api/tasks/{taskId}` | Member of task's project |
| PUT | `/api/tasks/{taskId}` | ADMIN any task; MEMBER only assigned tasks |
| DELETE | `/api/tasks/{taskId}` | Project ADMIN |

**Create/update task body:**

```json
{
  "title": "Design login page",
  "description": "Figma mockups",
  "status": "TODO",
  "dueDate": "2026-06-01",
  "assigneeId": 2
}
```

Status values: `TODO`, `IN_PROGRESS`, `DONE`

- Only **ADMIN** can set `assigneeId`.
- **MEMBER** can update tasks assigned to them.

---

## Dashboard

| Method | URL | Access |
|--------|-----|--------|
| GET | `/api/dashboard` | All projects for current user + stats |
| GET | `/api/dashboard/projects/{projectId}` | Per-project stats |

**Per-project dashboard includes:**

- `totalTasks`
- `statusCounts` (TODO, IN_PROGRESS, DONE)
- `overdueCount` and `overdueTasks` (due date before today and status ≠ DONE)

---

## Suggested Postman flow

1. **Signup** → copy `token`
2. Set collection variable `token` and header `Authorization: Bearer {{token}}`
3. **Create project**
4. **Signup** second user → **Add member** with their email
5. **Create task** (ADMIN assigns to member)
6. **Login** as member → update task status
7. **Dashboard** → verify overdue counts

---

## Project structure

```
com.teamtask.backend
├── config/          SecurityConfig
├── controller/      REST APIs
├── dto/             Request/response objects
├── entity/          JPA entities
├── enums/           Role, TaskStatus
├── exception/       GlobalExceptionHandler
├── repository/      Spring Data JPA
├── security/        JWT, UserPrincipal
└── service/         Business logic + RBAC
```

## RBAC summary

| Action | ADMIN | MEMBER |
|--------|-------|--------|
| Manage project settings | Yes | No |
| Add/remove/change members | Yes | No |
| Create tasks | Yes | Yes |
| Assign tasks | Yes | No |
| Update any task | Yes | No |
| Update assigned task | Yes | Yes (own only) |
| Delete tasks | Yes | No |
| View dashboard | Yes | Yes |
