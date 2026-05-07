#!/bin/bash
set -e

if [ -z "$DISPLAY" ]; then
    Xvfb :99 -screen 0 1280x800x24 &
    export DISPLAY=:99
    sleep 2
    x11vnc -display :99 -nopw -forever -quiet -rfbport 5900 &
    websockify --web /usr/share/novnc 6080 localhost:5900 &
    echo "[JavaFX] Interfazea: http://localhost:6080/vnc.html"
fi

exec java -jar app.jar
