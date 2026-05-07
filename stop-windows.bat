@echo off
echo Edukiontziak geldiarazten...
docker compose -f docker-compose.windows.yml down

echo.
echo Firewall regla ezabatzen (X11 / VcXsrv - puerto 6000)...
netsh advfirewall firewall delete rule name="VcXsrv X11 Docker" >nul 2>&1
if errorlevel 1 (
    echo  [ABISUA] Regla ez da aurkitu edo ezin da ezabatu.
) else (
    echo  Firewall regla ezabatuta: OK
)

echo.
echo Geldiaraztua. Datuak gordeta daude volume-an.
echo Dena ezabatzeko (BD barne): docker compose -f docker-compose.windows.yml down -v
echo.
pause