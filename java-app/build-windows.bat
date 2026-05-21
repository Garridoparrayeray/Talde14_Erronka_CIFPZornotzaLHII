@echo off
setlocal

set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.3.9-hotspot
set PATH=%PATH%;C:\maven\apache-maven-3.9.15\bin;%JAVA_HOME%\bin

echo ========================================
echo  Galdutakoak - Windows .exe sortzea
echo ========================================

echo.
echo [1/4] Compilando...
call mvn clean package -DskipTests
if %errorlevel% neq 0 (echo ERROR en compilacion & pause & exit /b 1)

echo.
echo [2/4] Copiando dependencias...
call mvn dependency:copy-dependencies -DoutputDirectory=target/libs
copy /Y target\galdutakoak-1.0-SNAPSHOT.jar target\libs\

echo.
echo [3/4] Eliminando build anterior...
if exist installer\Galdutakoak rmdir /s /q installer\Galdutakoak

echo.
echo [4/4] Creando .exe...
jpackage --input target\libs --name "Galdutakoak" --main-jar galdutakoak-1.0-SNAPSHOT.jar --main-class app.Launcher --type app-image --dest installer --app-version 1.0
if %errorlevel% neq 0 (echo ERROR en jpackage & pause & exit /b 1)

echo.
echo ========================================
echo  LISTO: installer\Galdutakoak\Galdutakoak.exe
echo  Mueve TODA la carpeta Galdutakoak/ donde quieras.
echo ========================================
pause
