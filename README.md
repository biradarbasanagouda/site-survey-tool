# 📡 Site Survey Tool — ISP Network Planning Platform

A full-stack monorepo for Internet Service Providers to plan, survey, and manage network equipment installations across large properties (MDUs, MTUs, campuses, parks).

---

## 🏗️ Project Structure

```
site-survey-tool/
├── backend/          # Spring Boot 3.x (Java 17) REST API
├── frontend/         # React 18 + Tailwind CSS SPA
├── docker/           # Docker Compose & environment configs
└── docs/             # Architecture & API documentation
```

---

## 🚀 Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | React 18, Tailwind CSS, React Router v6, Zustand, React Query |
| Backend | Spring Boot 3.2, Java 17, Spring Security, Spring Data JPA |
| Database | MySQL 8.0 |
| Auth | JWT (Access + Refresh tokens) |
| Storage | MinIO (S3-compatible) |
| Reporting | iText 7 (PDF generation) |
| Containerization | Docker + Docker Compose |

---

## ⚡ Quick Start

### Prerequisites
- Java 17+
- Node.js 18+
- Docker & Docker Compose
- Maven 3.8+

### 1. Start infrastructure (MySQL + MinIO)
```bash
cd docker
docker-compose up -d
```

### 2. Run Backend
```bash
cd backend
mvn spring-boot:run
```
API available at: `http://localhost:8080`

### 3. Run Frontend
```bash
cd frontend
npm install
npm run dev
```
App available at: `http://localhost:5173`

---

## 🗃️ Database

- Auto-migration via Flyway on startup
- Schema: `site_survey_db`
- Default credentials (dev): `root / root1234`

---

## 🔐 Authentication

JWT-based auth with access tokens (15 min) and refresh tokens (7 days).

Default seed users:
| Email | Password | Role |
|-------|----------|------|
| admin@isp.com | Admin@123 | SUPER_ADMIN |
| engineer@isp.com | Engineer@123 | FIELD_ENGINEER |

---

## 📦 Milestones

| Milestone | Scope | Status |
|-----------|-------|--------|
| M1 | Core Setup & Foundations | ✅ Scaffolded |
| M2 | Floor Plan & Data Import | ✅ Scaffolded |
| M3 | Labeling, Checklists & Data Capture | ✅ Scaffolded |
| M4 | Reporting, RF Integration & Finalization | ✅ Scaffolded |

---

## 📄 License
MIT
