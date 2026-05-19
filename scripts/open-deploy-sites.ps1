# Opens signup/deploy pages in your browser (run once)
Write-Host "Opening deployment sites..." -ForegroundColor Cyan
Start-Process "https://www.db4free.net/signup.php"
Start-Sleep -Seconds 1
Start-Process "https://dashboard.render.com/register"
Start-Sleep -Seconds 1
Start-Process "https://vercel.com/signup"
Write-Host "Complete signup on each tab, then run DEPLOY.md steps or ask the assistant for Step 3." -ForegroundColor Green
