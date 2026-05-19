# Run after filling deploy.secrets.ps1 (copy from deploy.secrets.example.ps1)
param(
    [string]$SecretsFile = "$PSScriptRoot\deploy.secrets.ps1"
)

if (-not (Test-Path $SecretsFile)) {
    Write-Host "Missing $SecretsFile" -ForegroundColor Red
    Write-Host "Copy deploy.secrets.example.ps1 to deploy.secrets.ps1 and fill in values."
    exit 1
}

. $SecretsFile

$VERCEL_FRONTEND = "https://team-task-manager-topaz-omega.vercel.app"
$RENDER_SERVICE = "team-task-manager-api"

Write-Host "=== Deploy checklist ===" -ForegroundColor Cyan
Write-Host "1. Open Render Blueprint:"
Write-Host "   https://dashboard.render.com/blueprint/new?repo=https://github.com/Kushdev77/team-task-manager"
Write-Host ""
Write-Host "2. Paste these Environment Variables:"
Write-Host "   SPRING_DATASOURCE_URL      = $SPRING_DATASOURCE_URL"
Write-Host "   SPRING_DATASOURCE_USERNAME = $SPRING_DATASOURCE_USERNAME"
Write-Host "   SPRING_DATASOURCE_PASSWORD = (hidden)"
Write-Host "   APP_CORS_ALLOWED_ORIGINS   = $VERCEL_FRONTEND"
Write-Host "   JWT_SECRET                 = (use generated or your value)"
Write-Host "   JPA_DDL_AUTO               = update"
Write-Host "   JPA_SHOW_SQL               = false"
Write-Host ""
Write-Host "3. After Live, test:"
Write-Host "   https://$RENDER_SERVICE.onrender.com/api/health"
Write-Host ""
Write-Host "4. Test signup:"
Write-Host "   $VERCEL_FRONTEND"
