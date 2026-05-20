# 🚀 Team Task Manager

A modern, full-stack, enterprise-grade project and task management system. Designed for collaborative team environments, it features role-based access control (Admin/Member), detailed project and team workflows, task progress tracking, and an interactive analytics dashboard.

---

## 🌐 Live Cloud Deployment

The application is deployed on cloud platforms, configured for zero-cost and maximum availability (24/7 online):

* **💻 Live Frontend Portal:** [https://team-task-manager-topaz-omega.vercel.app](https://team-task-manager-topaz-omega.vercel.app)
* **⚙️ Live Backend API Service:** [https://team-task-manager-api-h20v.onrender.com](https://team-task-manager-api-h20v.onrender.com)
* **📊 API Health Check Endpoint:** [https://team-task-manager-api-h20v.onrender.com/api/auth/signup](https://team-task-manager-api-h20v.onrender.com/api/auth/signup) *(Returns 401 Unauthorized / security active, confirming the API is fully alive)*

---

## 🛠️ Technology Stack

| Layer | Technology | Key Details |
| :--- | :--- | :--- |
| **Frontend** | **Angular 20 & RxJS** | Modern reactive UI, styled with custom Premium Vanilla CSS, smooth micro-interactions, responsive grids, and standard interceptors. |
| **Backend** | **Spring Boot 3.x** | Enterprise REST architecture with Jakarta validation, JPA Hibernate layer, and Spring Security. |
| **Security** | **JWT (JSON Web Tokens)** | Complete token-based stateless sessions, securely stored in local storage and passed via authorization headers. |
| **Database (Prod)** | **Embedded H2 Database** | Production builds run self-contained file H2 storage, allowing 100% free hosting 24/7 in cloud containers without external dependency. |
| **Database (Dev)** | **MySQL Server 8.x** | Local environment targets robust relational MySQL database instances. |

---

## 🚀 Key Features

* **🔑 Secure Authentication & RBAC:**
  - Standard User Registration & Login with encrypted passwords.
  - JWT token verification and claims extraction.
  - **Role-Based Access Control (Admin/Member):**
    - **Admins:** Can create projects, add members by email, delete/edit tasks, and manage project lifecycles.
    - **Members:** Can view project metrics and only update tasks specifically assigned to them.
* **📂 Project & Team Management:**
  - Group tasks by projects.
  - Invite registered users into projects dynamically by email address.
* **📋 Task Workflows:**
  - Create, assign, edit, and track status (`TODO`, `IN_PROGRESS`, `DONE`).
  - Set priorities and due dates.
* **📊 Analytics Dashboard:**
  - Live summary of total tasks, completion rates, and active assignments.
  - Overdue task alerts.

---

## ⚙️ Architecture & Database Strategy

To satisfy the **24/7 cloud availability** requirement while bypassing paid platform subscriptions (like Railway/Render database tiers), this application utilizes a dual-database model:
- **Local Dev:** Reads standard `application-local.properties` (gitignored) to run on local **MySQL** server on port `3306`.
- **Production Cloud:** Runs an embedded, self-contained **H2 relational database** engine. This eliminates cloud database hosting costs, avoids database sleep cycles, and ensures the live URL remains **100% active** instantly.

---

## 💻 Local Setup & Development

### 1. Prerequisites
- **Java 17+** & **Maven 3.x**
- **Node.js 20+** & **npm**
- **MySQL 8.x**

### 2. Database Initialization
Create your local MySQL database:
```sql
CREATE DATABASE IF NOT EXISTS team_task_db;
```

### 3. Running the Backend
Copy configuration and set your MySQL username/password:
- Location: `backend/src/main/resources/application.properties`

```powershell
cd backend
# Build and run
./mvnw.cmd spring-boot:run
```
*Wait until you see `Tomcat started on port 8080`.*
Verify health: `http://localhost:8080/api/health` ➡️ `{"status":"UP"}`

### 4. Running the Frontend
```powershell
cd "team task manager - frontend"
npm install
npm start
```
*Your frontend will open automatically at:* `http://localhost:4200`
*(Note: Dev environment uses proxy config to route `/api/*` to `localhost:8080` automatically).*

---

## 🔍 API Endpoint Overview

| Endpoint | Method | Security | Description |
| :--- | :--- | :--- | :--- |
| `/api/auth/signup` | `POST` | Public | Register a new user |
| `/api/auth/login` | `POST` | Public | Log in and receive a JWT token |
| `/api/projects` | `GET`/`POST` | Authenticated | Create or fetch user projects |
| `/api/projects/{id}/members` | `POST` | Admin Only | Add team members by email |
| `/api/tasks` | `GET`/`POST` | Authenticated | Manage project tasks |

*For complete endpoint details, view [backend/README.md](file:///c:/Users/kushd/OneDrive/Desktop/Team%20Task%20Manager/backend/README.md).*
