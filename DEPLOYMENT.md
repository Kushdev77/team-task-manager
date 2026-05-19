# Deployment Guide — Render + Vercel + MySQL

| Piece | Platform |
|-------|----------|
| MySQL database | Railway (free MySQL) |
| Spring Boot API | Render |
| Angular UI | Vercel |

## Order of steps

1. Create MySQL on Railway → copy credentials
2. Push deployment config to GitHub
3. Deploy backend on Render → copy API URL
4. Deploy frontend on Vercel → set API_URL
5. Add Vercel URL to Render CORS → redeploy backend
6. Test live signup

See each step in chat — one at a time.
