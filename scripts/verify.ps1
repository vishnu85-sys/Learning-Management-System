$ErrorActionPreference = 'Stop'

Write-Host '== LMS verification ==' -ForegroundColor Cyan

if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
  throw 'Maven is not on PATH. Install Maven first.'
}

Write-Host '[1/2] Java/Spring test suite' -ForegroundColor Yellow
mvn clean test
if ($LASTEXITCODE -ne 0) {
  throw "Maven test suite failed with exit code $LASTEXITCODE."
}

Write-Host '[2/2] Frontend JavaScript syntax' -ForegroundColor Yellow
node --check .\src\main\resources\static\app.js
if ($LASTEXITCODE -ne 0) {
  throw "Frontend JavaScript syntax check failed with exit code $LASTEXITCODE."
}

Write-Host 'Verification completed successfully.' -ForegroundColor Green
