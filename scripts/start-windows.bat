@echo off
REM ─────────────────────────────────────────────
REM  SiteSurvey Tool — Windows Quick Start Script
REM ─────────────────────────────────────────────

SET ROOT=%~dp0..
SET ROOT=%ROOT:\=/%

echo.
echo ╔══════════════════════════════════════╗
echo ║   SiteSurvey Tool — Quick Start      ║
echo ╚══════════════════════════════════════╝
echo.

REM ── Check Java ───────────────────────────────
java -version >nul 2>&1
IF ERRORLEVEL 1 (
    echo ❌  Java not found.
    echo     Install from: https://adoptium.net
    echo     Download: Temurin 17 Windows .msi installer
    pause
    exit /b 1
)
echo ✅  Java found
java -version 2>&1 | findstr version

REM ── Check Node ───────────────────────────────
node --version >nul 2>&1
IF ERRORLEVEL 1 (
    echo ❌  Node.js not found.
    echo     Install from: https://nodejs.org  ^(LTS version^)
    pause
    exit /b 1
)
echo ✅  Node found
node --version

REM ── Check MySQL ──────────────────────────────
mysql --version >nul 2>&1
IF ERRORLEVEL 1 (
    echo ❌  MySQL not found.
    echo     Install from: https://dev.mysql.com/downloads/installer/
    echo     Choose: MySQL Installer for Windows
    pause
    exit /b 1
)
echo ✅  MySQL found

REM ── Create DB ────────────────────────────────
echo.
echo ▶  Setting up database...
mysql -u root -e "CREATE DATABASE IF NOT EXISTS site_survey_db CHARACTER SET utf8mb4;" 2>nul
mysql -u root -e "CREATE USER IF NOT EXISTS 'sst_user'@'localhost' IDENTIFIED BY 'sst_pass';" 2>nul
mysql -u root -e "GRANT ALL PRIVILEGES ON site_survey_db.* TO 'sst_user'@'localhost'; FLUSH PRIVILEGES;" 2>nul
echo ✅  Database ready

REM ── Install frontend deps ────────────────────
echo.
echo ▶  Installing frontend dependencies (first time only)...
cd /d "%ROOT%\frontend"
IF NOT EXIST "node_modules" (
    npm install
)
echo ✅  Frontend dependencies ready

REM ── Start Backend ────────────────────────────
echo.
echo ▶  Starting Backend (Spring Boot)...
cd /d "%ROOT%\backend"
start "SiteSurvey Backend" cmd /c "mvn spring-boot:run && pause"
echo    Backend starting in new window, please wait 20 seconds...

REM ── Start Frontend ───────────────────────────
echo ▶  Starting Frontend (React)...
cd /d "%ROOT%\frontend"
start "SiteSurvey Frontend" cmd /c "npm run dev && pause"

REM ── Wait ─────────────────────────────────────
echo.
echo ⏳  Waiting 25 seconds for backend to start...
timeout /t 25 /nobreak >nul

REM ── Open browser ─────────────────────────────
echo.
echo ╔══════════════════════════════════════════╗
echo ║  🚀  App is running!                     ║
echo ║                                          ║
echo ║  Frontend : http://localhost:5173        ║
echo ║  Backend  : http://localhost:8080        ║
echo ║                                          ║
echo ║  Login    : admin@isp.com                ║
echo ║  Password : Admin@123                    ║
echo ╚══════════════════════════════════════════╝
echo.

start http://localhost:5173

echo Press any key to stop all services...
pause >nul

REM ── Cleanup ──────────────────────────────────
taskkill /FI "WINDOWTITLE eq SiteSurvey Backend" /F >nul 2>&1
taskkill /FI "WINDOWTITLE eq SiteSurvey Frontend" /F >nul 2>&1
echo Done. Goodbye!
