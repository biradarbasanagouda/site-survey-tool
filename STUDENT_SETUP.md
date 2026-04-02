# 🚀 SiteSurvey Tool — Student Setup Guide

Get the app running in **under 10 minutes** on Mac or Windows. No Docker needed.

---

## What you need to install (one time only)

| Tool | Download Link | Version |
|------|--------------|---------|
| **Java 17** | https://adoptium.net | Temurin 17 LTS |
| **Maven** | https://maven.apache.org/download.cgi | 3.8+ |
| **Node.js** | https://nodejs.org | LTS (20+) |
| **MySQL** | https://dev.mysql.com/downloads/mysql/ | 8.0 |

> **Mac shortcut** — if you have Homebrew:
> ```bash
> brew install openjdk@17 maven node mysql
> brew services start mysql
> ```

---

## Step 1 — Create the database

Open a terminal and run:

```bash
mysql -u root -e "CREATE DATABASE IF NOT EXISTS site_survey_db;"
```

If your MySQL root has a password:
```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS site_survey_db;"
```

---

## Step 2 — Configure your MySQL password

Open `backend/src/main/resources/application.yml` and set your MySQL root password:

```yaml
spring:
  datasource:
    username: root
    password:          # ← put your MySQL root password here (leave blank if none)
```

---

## Step 3 — Start the Backend

Open a terminal in the project root:

```bash
cd backend
mvn spring-boot:run
```

Wait about **20–30 seconds**. You should see:
```
Started SiteSurveyApplication in 8.3 seconds
```

✅ Backend running at: **http://localhost:8080**

---

## Step 4 — Start the Frontend

Open a **new terminal** in the project root:

```bash
cd frontend
npm install        # first time only — takes ~1 minute
npm run dev
```

✅ Frontend running at: **http://localhost:5173**

---

## Step 5 — Open the app

Go to **http://localhost:5173** in your browser.

Login with:
| Email | Password | Role |
|-------|----------|------|
| `admin@isp.com` | `Admin@123` | Admin |
| `engineer@isp.com` | `Engineer@123` | Field Engineer |

---

## Quick Start Scripts

Instead of steps 3 & 4, you can use the scripts:

**Mac:**
```bash
chmod +x scripts/start-mac.sh
./scripts/start-mac.sh
```

**Windows:**
```
Double-click scripts\start-windows.bat
```

---

## Troubleshooting

### ❌ "Access denied for user 'root'"
Your MySQL has a password. Edit `application.yml` and add it under `password:`.

### ❌ "Port 8080 already in use"
Something else is using port 8080. Kill it:
```bash
# Mac/Linux
lsof -ti:8080 | xargs kill -9

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### ❌ "npm install" fails
Make sure Node.js 18+ is installed:
```bash
node --version   # should be v18 or v20
```

### ❌ Backend starts but frontend shows blank page
Make sure both are running simultaneously — backend in one terminal, frontend in another.

### ❌ File upload doesn't work
That's expected in the simple setup — MinIO (file storage) is not running. All other features work fine.

---

## Project Structure

```
site-survey-tool/
├── backend/          ← Spring Boot Java API  (port 8080)
├── frontend/         ← React app             (port 5173)
├── docker/           ← Docker setup (optional, skip for demo)
└── scripts/
    ├── start-mac.sh       ← One-click Mac start
    └── start-windows.bat  ← One-click Windows start
```

---

## API Endpoints (for testing)

```bash
# Health check
curl http://localhost:8080/actuator/health

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@isp.com","password":"Admin@123"}'
```
