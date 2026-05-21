set -e

echo "Iniciando Xvfb..."
Xvfb :99 -screen 0 1920x1080x24 &
XVFB_PID=$!

# Esperar hasta que Xvfb esté listo (máximo 10s)
for i in $(seq 1 20); do
    xdpyinfo -display :99 >/dev/null 2>&1 && break
    sleep 0.5
done

export DISPLAY=:99

echo "Iniciando x11vnc..."
x11vnc -display :99 -forever -nopw -rfbport 5900 &
X11VNC_PID=$!
sleep 1

echo "Iniciando noVNC/websockify..."
websockify --web=/usr/share/novnc/ 6080 localhost:5900 &

echo "Interfazea: http://localhost:6080/vnc.html?autoconnect=1?resize=scale&quality=high"
echo "Iniciando aplicación Java..."
exec java --add-opens java.base/java.lang=ALL-UNNAMED -jar app.jar
