# 🚀 Pushing Site Survey Tool to GitHub

## Step 1 — Create a new GitHub repository

Go to https://github.com/new and create a repository:
- **Name**: `site-survey-tool`
- **Visibility**: Private or Public (your choice)
- **DO NOT** initialise with README, .gitignore, or licence (the project already has them)

---

## Step 2 — Initialise Git and push

After extracting the ZIP, open a terminal in the `site-survey-tool/` folder and run:

```bash
# Initialise git
git init

# Add all files
git add .

# First commit
git commit -m "feat: initial scaffold — Spring Boot + React ISP Site Survey Tool

- Full DB schema (14 tables) with Flyway migrations
- JWT auth (access + refresh tokens)
- REST API: properties, buildings, floors, spaces, equipment,
  checklists, RF scans, reports, cable paths, bulk import
- React 18 + Tailwind CSS frontend with Zustand + React Query
- Docker Compose for local dev (MySQL + MinIO + backend + frontend)
- GitHub Actions CI pipeline"

# Add your remote (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/site-survey-tool.git

# Push
git branch -M main
git push -u origin main
```

---

## Step 3 — Set up branch strategy

```bash
# Create a develop branch for ongoing work
git checkout -b develop
git push -u origin develop

# Feature branch workflow
git checkout -b feature/floor-plan-canvas
# ... work ...
git push origin feature/floor-plan-canvas
# Open PR → develop → main
```

---

## Step 4 — Configure GitHub Actions secrets

Go to **Settings → Secrets and Variables → Actions** in your repo and add:

| Secret Name | Value |
|---|---|
| `JWT_SECRET` | A 64+ character random string |
| `DB_PASSWORD` | Your production DB password |
| `MINIO_SECRET_KEY` | Your MinIO secret |

---

## Step 5 — Start local development

```bash
# 1. Start infrastructure
cd docker
docker-compose up -d mysql minio
# Wait ~15 seconds for MySQL to be ready

# 2. Start backend
cd ../backend
mvn spring-boot:run
# API at http://localhost:8080
# Flyway auto-migrates the DB on startup

# 3. Start frontend (new terminal)
cd ../frontend
npm install
npm run dev
# App at http://localhost:5173
```

### Default login credentials
| Email | Password | Role |
|---|---|---|
| admin@isp.com | Admin@123 | SUPER_ADMIN |
| engineer@isp.com | Engineer@123 | FIELD_ENGINEER |

---

## Step 6 — Verify API is working

```bash
# Health check
curl http://localhost:8080/actuator/health

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@isp.com","password":"Admin@123"}'

# List properties (use token from login)
curl http://localhost:8080/api/properties?orgId=1 \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

## Step 7 — Run tests

```bash
cd backend
mvn test
# Uses H2 in-memory DB — no MySQL needed for tests
```

---

## Step 8 — Full Docker deployment

```bash
cd docker
docker-compose up --build -d
# All 4 services: MySQL, MinIO, Backend, Frontend
# App at http://localhost:80
```

---

## Project structure recap

```
site-survey-tool/
├── .github/workflows/ci.yml    ← GitHub Actions CI
├── backend/
│   ├── src/main/java/…         ← Spring Boot source
│   ├── src/test/java/…         ← Unit + integration tests
│   ├── src/main/resources/
│   │   ├── application.yml     ← Dev config
│   │   ├── application-test.yml← Test config (H2)
│   │   └── db/migration/       ← Flyway SQL scripts
│   ├── Dockerfile
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── api/                ← Typed Axios clients
│   │   ├── components/         ← Reusable UI components
│   │   ├── pages/              ← Route pages
│   │   ├── store/              ← Zustand auth store
│   │   └── types/              ← TypeScript interfaces
│   ├── Dockerfile
│   └── nginx.conf
├── docker/
│   └── docker-compose.yml
└── README.md
```

---

## Next development steps (suggested)

1. **Floor plan canvas** — integrate MapLibre GL JS for interactive space tagging
2. **RF heatmap overlay** — display parsed Kismet/Vistumbler data on floor plans  
3. **PDF report template** — implement iText 7 report generation in `ReportServiceImpl`
4. **Mobile offline sync** — Flutter client with SQLite → REST sync queue
5. **Role-based UI** — hide admin controls for FIELD_ENGINEER role
6. **Real-time status** — WebSocket for report generation progress
