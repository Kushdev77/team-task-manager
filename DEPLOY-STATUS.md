# Live deployment status — UPDATED

## Frontend (Vercel) — LIVE

**URL:** https://team-task-manager-topaz-omega.vercel.app

## Backend — LIVE via tunnel (your PC must stay on)

**API:** https://good-geese-laugh.loca.lt/api

- Spring Boot running locally (MySQL on your machine)
- Public tunnel: localtunnel (background process)
- **Signup should work now** while tunnel + backend are running

### Important
- If you **close PC / stop backend / stop tunnel**, signup will break again
- Tunnel URL **changes** each time you restart localtunnel

## Permanent backend (Render) — ready to deploy, no db4free needed

GitHub has `render.yaml` with **H2 database on Render** (no MySQL signup).

1. Sign up: https://dashboard.render.com/register
2. Open: https://dashboard.render.com/blueprint/new?repo=https://github.com/Kushdev77/team-task-manager
3. Click **Apply** (env vars are pre-filled in blueprint)
4. After Live, update Vercel `API_URL` to `https://team-task-manager-api.onrender.com/api`

## Restart live stack (tunnel)

```powershell
cd "C:\Users\kushd\OneDrive\Desktop\Team Task Manager\scripts"
.\start-live-stack.ps1
```
