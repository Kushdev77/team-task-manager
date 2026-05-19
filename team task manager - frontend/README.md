# Team Task Manager — Frontend

Angular 20 SPA for the Team Task Manager backend API.

## Prerequisites

- Node.js 18+
- Backend running at `http://localhost:8080`

## Run

```bash
cd task-manager-frontend
npm install
ng serve
```

Open **http://localhost:4200**

## Configuration

API URL: `src/environments/environment.ts`

```typescript
apiUrl: 'http://localhost:8080/api'
```

## Features

- Sign up / Login (JWT stored in localStorage)
- Dashboard with project stats and overdue tasks
- Projects CRUD (admin can delete)
- Project detail: Kanban tasks, team members, overview
- My Tasks (assigned tasks)

## Test flow

1. Start backend: `cd backend && mvn spring-boot:run`
2. Start frontend: `ng serve`
3. Sign up as admin → create project
4. Sign up as member (incognito or another browser)
5. Admin adds member by email → create & assign tasks
