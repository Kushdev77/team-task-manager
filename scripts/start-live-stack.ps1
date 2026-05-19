# Starts local backend + public tunnel, then updates Vercel API_URL (run while PC is on)
$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$backend = Join-Path $root "backend"
$frontend = Join-Path $root "team task manager - frontend"

Write-Host "Starting Spring Boot backend..." -ForegroundColor Cyan
$backendJob = Start-Job -ScriptBlock {
    Set-Location $using:backend
    & .\mvnw.cmd -q spring-boot:run 2>&1
}

Start-Sleep -Seconds 45
try {
    $health = Invoke-RestMethod -Uri "http://localhost:8080/api/health" -TimeoutSec 10
    Write-Host "Local backend OK: $($health | ConvertTo-Json -Compress)" -ForegroundColor Green
} catch {
    Write-Host "Backend not ready yet. Check job logs: Receive-Job -Id $($backendJob.Id)" -ForegroundColor Yellow
}

Write-Host "Starting localtunnel on port 8080..." -ForegroundColor Cyan
Set-Location $frontend
$tunnelJob = Start-Job -ScriptBlock {
    npx --yes localtunnel --port 8080 2>&1
}
Start-Sleep -Seconds 15
$tunnelOut = Receive-Job -Id $tunnelJob.Id
$tunnelOut | ForEach-Object { Write-Host $_ }
$urlLine = $tunnelOut | Where-Object { $_ -match "https://.*\.loca\.lt" } | Select-Object -First 1
if ($urlLine -match "(https://[^\s]+)") {
    $tunnelUrl = $Matches[1]
    $apiUrl = "$tunnelUrl/api"
    Write-Host "`nTunnel URL: $apiUrl" -ForegroundColor Green
    Write-Host "Updating Vercel API_URL and redeploying..."
    echo $apiUrl | npx vercel env rm API_URL production -y 2>$null
    echo $apiUrl | npx vercel env add API_URL production
    npx vercel deploy --prod --yes
    Write-Host "`nLive frontend: https://team-task-manager-topaz-omega.vercel.app" -ForegroundColor Green
    Write-Host "Keep this PowerShell window open while demoing." -ForegroundColor Yellow
} else {
    Write-Host "Could not parse tunnel URL. Run manually: npx localtunnel --port 8080" -ForegroundColor Red
}
