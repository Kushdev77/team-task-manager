# Finish deployment — do these 2 signups (15 min)

Browser tabs should be open for:
- db4free signup
- Render signup

## A) db4free (free MySQL) — 5 min

1. Sign up at https://www.db4free.net/signup.php
2. After login, note:
   - **Database name** (e.g. `yourname_task`)
   - **Username** (your db4free login)
   - **Password**
3. Host is always: `db4free.net`, port `3306`

## B) Render (backend host) — 5 min

1. Sign up at https://dashboard.render.com/register (GitHub login is easiest)
2. Do NOT deploy yet — reply here with db4free details first

## C) Reply to assistant with (no password in public chat if you prefer)

Create file `scripts/deploy.secrets.ps1` from `scripts/deploy.secrets.example.ps1` OR reply:

```
DB_NAME=your_database_name
DB_USER=your_username
DB_PASS=your_password
```

Then say: **credentials ready**

Assistant will:
- Print exact Render env vars
- Open Render Blueprint
- Fix Vercel API_URL after backend is live
- Verify signup works

## Live frontend (already done)

https://team-task-manager-topaz-omega.vercel.app
