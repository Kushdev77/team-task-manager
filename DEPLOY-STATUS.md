# Live deployment status

## Frontend (Vercel) — LIVE

**Production URL:** https://team-task-manager-topaz-omega.vercel.app

- Project: `team-task-manager` on Vercel
- `API_URL` env: `https://team-task-manager-api.onrender.com/api`

## Backend (Render) — PENDING

Expected URL: `https://team-task-manager-api.onrender.com`

Complete in browser (tabs should be open):
1. db4free.net — create MySQL, copy credentials
2. Render Blueprint — https://dashboard.render.com/blueprint/new?repo=https://github.com/Kushdev77/team-task-manager
3. Paste db4free env vars when Render asks
4. Set `APP_CORS_ALLOWED_ORIGINS` = `https://team-task-manager-topaz-omega.vercel.app`

## After Render is live

Redeploy not needed on Vercel if API_URL is correct. Test signup on the Vercel URL.
