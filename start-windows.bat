@echo off
REM =============================================
REM  ERRONKA - Windows 11 abiarazlea
REM =============================================
setlocal

echo.
echo ==========================================
echo   ERRONKA - Bermeoko Udala
echo ==========================================
echo.

REM --- 1. Docker Desktop egiaztatu ---
echo [1/5] Docker Desktop egiaztatzen...
docker info >nul 2>&1
if errorlevel 1 (
    echo.
    echo  [ERROR] Docker Desktop ez dago martxan.
    echo  Ireki Docker Desktop, itxaron prest egon arte, eta saiatu berriro.
    echo.
    pause
    exit /b 1
)

REM --- 2. Firewall regla VcXsrv-rako (X11 puerto 6000) ---
echo [2/5] Firewall regla egiaztatzen (X11 / VcXsrv)...
netsh advfirewall firewall show rule name="VcXsrv X11 Docker" >nul 2>&1
if errorlevel 1 (
    echo  Firewall regla sortzen (X11 puerto 6000 Docker-entzat)...
    netsh advfirewall firewall add rule name="VcXsrv X11 Docker" dir=in action=allow protocol=TCP localport=6000 >nul 2>&1
    if errorlevel 1 (
        echo.
        echo  [ABISUA] Ezin da firewall regla sortu. Administratzaile gisa exekutatu.
        echo  Eskuz egin: puerto 6000 TCP sarrerako trafikoa baimendu.
        echo.
    ) else (
        echo  Firewall regla sortuta: OK
    )
) else (
    echo  Firewall regla dagoeneko dago: OK
)

REM --- 3. VcXsrv egiaztatu (JavaFX-erako) ---
echo [3/5] VcXsrv (X server) egiaztatzen...
tasklist /FI "IMAGENAME eq vcxsrv.exe" 2>nul | find /I "vcxsrv.exe" >nul
if errorlevel 1 (
    echo.
    echo  [ABISUA] VcXsrv ez dago martxan.
    echo  JavaFX leihoa erakusteko VcXsrv abiarazi behar da.
    echo.
    echo  Egiaztatu:
    echo    1. VcXsrv instalatuta dagoen   (https://sourceforge.net/projects/vcxsrv/)
    echo    2. XLaunch programa abiaraztu honela:
    echo         - Multiple windows
    echo         - Start no client
    echo         - [X] Disable access control       ^<-- GARRANTZITSUA!
    echo         - [X] Native opengl                ^<-- demarkatu hau
    echo.
    set /p continue="VcXsrv abiarazi duzu eta jarraitu nahi duzu? (b/e): "
    if /i not "%continue%"=="b" exit /b 1
)

REM --- 4. Konposatu ---
echo [4/5] Edukiontziak eraikitzen eta abiarazten...
docker compose -f docker-compose.windows.yml up --build -d

if errorlevel 1 (
    echo.
    echo  [ERROR] Errorea konposatzean.
    pause
    exit /b 1
)

REM --- 5. Egoera ---
echo.
echo [5/5] Egoera:
docker compose -f docker-compose.windows.yml ps

echo.
echo ==========================================
echo   PRESTATUTA!
echo.
echo   Web portala:    http://localhost:8000
echo   Adminer (BD):   http://localhost:8081
echo                   Server:  db
echo                   User:    bermeo_udaltzain
echo                   Pass:    udaltzainpw
echo                   DB:      erronka_galduak
echo.
echo   JavaFX:         VcXsrv leiho gisa agertuko da
echo.
echo   Geldiarazteko:  stop-windows.bat
echo ==========================================
echo.
pause