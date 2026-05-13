@echo off
setlocal

echo ========================================
echo  Galdutakoak - Windows .exe sortzea
echo ========================================

REM Java 21 egiaztatu
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java ez dago instalatuta. Instalatu Java 21 JDK.
    echo https://adoptium.net/temurin/releases/?version=21
    pause
    exit /b 1
)

REM Maven egiaztatu
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven ez dago instalatuta. Instalatu Maven.
    echo https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

REM jpackage egiaztatu
jpackage --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: jpackage ez dago. Java 21 JDK beharrezkoa da (JRE ez).
    pause
    exit /b 1
)

echo.
echo [1/2] JAR eraikitzen...
call mvn package -q
if %errorlevel% neq 0 (
    echo ERROR: Maven build huts egin du.
    pause
    exit /b 1
)
echo     JAR sortuta: target\galdutakoak-1.0-SNAPSHOT.jar

echo.
echo [2/2] .exe sortzen...
set JAVAFX_PATH=%USERPROFILE%\.m2\repository\org\openjfx

if exist target\Galdutakoak rmdir /s /q target\Galdutakoak

jpackage ^
  --input target ^
  --main-jar galdutakoak-1.0-SNAPSHOT.jar ^
  --main-class app.Launcher ^
  --module-path "%JAVAFX_PATH%" ^
  --add-modules javafx.controls,javafx.fxml ^
  --name Galdutakoak ^
  --app-version 1.0 ^
  --vendor "Bermeoko Udala" ^
  --type exe ^
  --dest target

if %errorlevel% neq 0 (
    echo ERROR: jpackage huts egin du.
    pause
    exit /b 1
)

echo.
echo ========================================
echo  DONE: target\Galdutakoak-1.0.exe
echo ========================================
echo  Docker martxan egon behar da aurretik:
echo    docker compose up -d
echo ========================================
pause
