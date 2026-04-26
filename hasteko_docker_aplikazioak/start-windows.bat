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
echo [1/4] Docker Desktop egiaztatzen...
docker info >nul 2>&1
if errorlevel 1 (
    echo.
    echo  [ERROR] Docker Desktop ez dago martxan.
    echo  Ireki Docker Desktop, itxaron prest egon arte, eta saiatu berriro.
    echo.
    pause
    exit /b 1
)

REM --- 2. VcXsrv egiaztatu (JavaFX-erako) ---
echo [2/4] VcXsrv (X server) egiaztatzen...
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
    echo.
    set /p continue="VcXsrv abiarazi duzu eta jarraitu nahi duzu? (b/e): "
    if /i not "%continue%"=="b" exit /b 1
)

REM --- 3. Konposatu ---
echo [3/4] Edukiontziak eraikitzen eta abiarazten...
docker compose -f docker-compose.windows.yml up --build -d

if errorlevel 1 (
    echo.
    echo  [ERROR] Errorea konposatzean.
    pause
    exit /b 1
)

REM --- 4. Egoera ---
echo.
echo [4/4] Egoera:
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