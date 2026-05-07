#!/bin/bash
# ===========================================
# ERRONKA - Linux abiarazlea
# ===========================================
set -e

echo ""
echo "=========================================="
echo "  ERRONKA - Bermeoko Udala"
echo "=========================================="
echo ""

echo "[1/2] Edukiontziak eraikitzen eta abiarazten..."
docker compose up --build -d

echo ""
echo "[2/2] Egoera:"
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
echo "  JavaFX app:     http://localhost:6080/vnc.html?autoconnect=1&resize=scale"
echo "                  (10-15s itxaron)"
echo ""
echo "  Logak:          docker compose logs -f java-app"
echo "  Geldiarazteko:  ./stop-linux.sh"
echo "=========================================="
