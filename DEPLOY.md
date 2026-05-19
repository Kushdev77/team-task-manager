# One-page deploy checklist (Render + Vercel + db4free)

## Already done on your PC
- Deployment config committed and pushed to GitHub
- Backend uses environment variables for production

## You must do in browser (cannot be automated — accounts are yours)

### A. Database — db4free.net (5 min)
1. Sign up: https://www.db4free.net/signup.php
2. Note: host `db4free.net`, port `3306`, database name, username, password
3. JDBC URL for Render:
   `jdbc:mysql://db4free.net:3306/YOUR_DB?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`

### B. Backend — Render (10 min)
1. https://dashboard.render.com → New → Web Service
2. Connect repo: `Kushdev77/team-task-manager`
3. **Root Directory:** `backend`
4. **Build:** `./mvnw -DskipTests clean package`
5. **Start:** `java -jar target/backend-0.0.1-SNAPSHOT.jar`
6. **Environment variables:**

| Key | Value |
|-----|--------|
| `SPRING_DATASOURCE_URL` | (your JDBC URL from A) |
| `SPRING_DATASOURCE_USERNAME` | db4free username |
| `SPRING_DATASOURCE_PASSWORD` | db4free password |
| `JWT_SECRET` | any long random string 40+ chars |
| `APP_CORS_ALLOWED_ORIGINS` | `https://YOUR-VERCEL-APP.vercel.app` (set after Vercel) |
| `JPA_DDL_AUTO` | `update` |

7. Deploy → copy URL e.g. `https://team-task-manager-api.onrender.com`
8. Test: `https://YOUR-RENDER-URL.onrender.com/api/health`

### C. Frontend — Vercel (5 min)
1. https://vercel.com/new → Import `Kushdev77/team-task-manager`
2. **Root Directory:** `team task manager - frontend`
3. **Environment variable:**

| Key | Value |
|-----|--------|
| `API_URL` | `https://YOUR-RENDER-URL.onrender.com/api` |

4. Deploy → copy Vercel URL

### D. Finish CORS
1. Render → your service → Environment → set `APP_CORS_ALLOWED_ORIGINS` to your Vercel URL (no trailing slash)
2. Manual Deploy → redeploy

### E. Test live
Open Vercel URL → Sign up → Create project → Add task
