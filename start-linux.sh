#!/bin/bash
# ===========================================
# ERRONKA - Linux abiarazlea
# ===========================================
# chmod +x start-linux.sh stop-linux.sh ejekutableak egiteko
set -e

echo ""
echo "=========================================="
echo "  ERRONKA - Bermeoko Udala"
echo "=========================================="
echo ""

echo "[1/3] X11 baimendu Docker-ekin (JavaFX leihoetarako)..."
xhost +local:docker > /dev/null

echo "[2/3] Edukiontziak eraikitzen eta abiarazten..."
docker compose up --build -d

echo ""
echo "[3/3] Egoera:"
docker compose ps

echo ""
echo "=========================================="
echo "  PRESTATUTA!"
echo ""
echo "  Web portala:    http://localhost:8000"
echo "  Adminer (BD):   http://localhost:8081"
echo "                  Server:   db"
echo "                  User:     bermeo_udaltzain"
echo "                  Pass:     udaltzainpw"
echo "                  DB:       erronka_galduak"
echo ""
echo "  JavaFX:         leiho gisa agertuko da"
echo ""
echo "  Logak ikusteko:    docker compose logs -f"
echo "  Geldiarazteko:     ./stop-linux.sh"
echo "=========================================="