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
echo [1/3] Docker Desktop egiaztatzen...
docker info >nul 2>&1
if errorlevel 1 (
    echo.
    echo  [ERROR] Docker Desktop ez dago martxan.
    echo  Ireki Docker Desktop, itxaron prest egon arte, eta saiatu berriro.
    echo.
    pause
    exit /b 1
)

REM --- 2. Konposatu ---
echo [2/3] Edukiontziak eraikitzen eta abiarazten...
docker compose -f docker-compose.windows.yml up --build -d

if errorlevel 1 (
    echo.
    echo  [ERROR] Errorea konposatzean.
    pause
    exit /b 1
)

REM --- 3. Egoera ---
echo.
echo [3/3] Egoera:
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
echo   JavaFX app:     http://localhost:6080/vnc.html
echo                   (nabigatzailean ireki, 10-15s itxaron)
echo.
echo   Geldiarazteko:  stop-windows.bat
echo ==========================================
echo.
pause
