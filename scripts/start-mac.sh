#!/bin/bash
# ─────────────────────────────────────────────
#  SiteSurvey Tool — Mac Quick Start Script
# ─────────────────────────────────────────────

ROOT="$(cd "$(dirname "$0")/.." && pwd)"

echo ""
echo "╔══════════════════════════════════════╗"
echo "║   SiteSurvey Tool — Quick Start      ║"
echo "╚══════════════════════════════════════╝"
echo ""

# ── Check Java ───────────────────────────────
if ! command -v java &>/dev/null; then
  echo "❌  Java not found."
  echo "    Install from: https://adoptium.net"
  echo "    Download: Temurin 17 → macOS → .pkg"
  exit 1
fi
echo "✅  Java: $(java -version 2>&1 | head -1)"

# ── Check Node ───────────────────────────────
if ! command -v node &>/dev/null; then
  echo "❌  Node.js not found."
  echo "    Install from: https://nodejs.org  (LTS version)"
  exit 1
fi
echo "✅  Node: $(node --version)"

# ── Check MySQL ──────────────────────────────
if ! command -v mysql &>/dev/null; then
  echo ""
  echo "❌  MySQL not found."
  echo "    Install options:"
  echo "    1) brew install mysql   (if you have Homebrew)"
  echo "    2) Download from: https://dev.mysql.com/downloads/mysql/"
  echo ""
  exit 1
fi
echo "✅  MySQL found"

# ── Start MySQL ──────────────────────────────
echo ""
echo "▶  Starting MySQL..."
brew services start mysql 2>/dev/null || mysql.server start 2>/dev/null || true
sleep 2

# ── Create DB & User ─────────────────────────
echo "▶  Setting up database..."
mysql -u root -e "
  CREATE DATABASE IF NOT EXISTS site_survey_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
  CREATE USER IF NOT EXISTS 'sst_user'@'localhost' IDENTIFIED BY 'sst_pass';
  GRANT ALL PRIVILEGES ON site_survey_db.* TO 'sst_user'@'localhost';
  FLUSH PRIVILEGES;
" 2>/dev/null || \
mysql -u root -proot1234 -e "
  CREATE DATABASE IF NOT EXISTS site_survey_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
" 2>/dev/null || true

echo "✅  Database ready"

# ── Install frontend deps ────────────────────
echo ""
echo "▶  Installing frontend dependencies (first time only)..."
cd "$ROOT/frontend"
if [ ! -d "node_modules" ]; then
  npm install --silent
fi
echo "✅  Frontend dependencies ready"

# ── Start Backend ────────────────────────────
echo ""
echo "▶  Starting Backend (Spring Boot)..."
cd "$ROOT/backend"
mvn spring-boot:run -q \
  -Dspring-boot.run.jvmArguments="-Dlogging.level.root=WARN" &
BACKEND_PID=$!
echo "   Backend PID: $BACKEND_PID (starting, please wait 20s...)"

# ── Start Frontend ───────────────────────────
echo "▶  Starting Frontend (React)..."
cd "$ROOT/frontend"
npm run dev &
FRONTEND_PID=$!

# ── Wait and open browser ────────────────────
echo ""
echo "⏳  Waiting for backend to start..."
for i in {1..30}; do
  sleep 2
  if curl -s http://localhost:8080/actuator/health | grep -q "UP" 2>/dev/null; then
    echo "✅  Backend is UP!"
    break
  fi
  echo "   Still starting... ($((i*2))s)"
done

echo ""
echo "╔══════════════════════════════════════════╗"
echo "║  🚀  App is running!                     ║"
echo "║                                          ║"
echo "║  Frontend : http://localhost:5173        ║"
echo "║  Backend  : http://localhost:8080        ║"
echo "║                                          ║"
echo "║  Login    : admin@isp.com                ║"
echo "║  Password : Admin@123                    ║"
echo "║                                          ║"
echo "║  Press Ctrl+C to stop everything        ║"
echo "╚══════════════════════════════════════════╝"
echo ""

open http://localhost:5173 2>/dev/null || true

# ── Cleanup on exit ──────────────────────────
cleanup() {
  echo ""
  echo "Stopping services..."
  kill $BACKEND_PID $FRONTEND_PID 2>/dev/null
  echo "Done. Goodbye!"
}
trap cleanup EXIT INT TERM
wait
