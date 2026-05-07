set -e

echo "Iniciando Xvfb..."
Xvfb :99 -screen 0 1600x900x24 &
XVFB_PID=$!
sleep 2

export DISPLAY=:99

echo "Iniciando x11vnc..."
x11vnc -display :99 -forever -nopw -rfbport 5900 &
X11VNC_PID=$!
sleep 2

echo "Iniciando noVNC/websockify..."
websockify --web=/usr/share/novnc/ 6080 localhost:5900 &

echo "Interfazea: http://localhost:6080/vnc.html?autoconnect=1&resize=scale"
echo "Iniciando aplicación Java..."
exec java --add-opens java.base/java.lang=ALL-UNNAMED -jar app.jar
