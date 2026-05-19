# Team Task Manager

Full-stack web app: projects, tasks, team members, role-based access (Admin/Member), JWT auth, dashboard.

## Stack

| Layer | Tech |
|-------|------|
| Backend | Spring Boot 4, MySQL, JWT |
| Frontend | Angular 20 |

## Prerequisites

1. **Java 17+** and **Maven**
2. **Node.js 20+** and **npm**
3. **MySQL** — create database:

```sql
CREATE DATABASE IF NOT EXISTS team_task_db;
```

4. Copy `backend/src/main/resources/application.properties.example` → `application.properties` and set your MySQL username/password.

## Run locally

**Terminal 1 — Backend**

```powershell
cd backend
mvn spring-boot:run
```

Wait until you see `Tomcat started on port 8080`. Test: http://localhost:8080/api/health → `{"status":"UP"}`

**Terminal 2 — Frontend**

```powershell
cd "team task manager - frontend\task-manager-frontend"
npm install
npm start
```

Open http://localhost:4200

The dev server proxies `/api` → `http://localhost:8080` (no CORS issues).

## Quick test flow

1. **Sign up** — new account
2. **Create project** — you become Admin
3. **Add task** — assign to yourself or leave unassigned
4. **Sign up second user** (incognito) — add their email under Members
5. **Member** updates only their assigned tasks; **Admin** manages all

## Features (assignment requirements)

- Authentication (signup/login, JWT)
- Projects & team members (Admin adds by email)
- Tasks: create, assign, status (TODO / IN_PROGRESS / DONE)
- Dashboard: totals, status counts, overdue tasks
- REST APIs + MySQL + validations + RBAC

## API base

`http://localhost:8080/api` — see `backend/README.md` for endpoints.
