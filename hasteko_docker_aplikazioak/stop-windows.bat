@echo off
echo Edukiontziak geldiarazten...
docker compose -f docker-compose.windows.yml down
echo.
echo Geldiaraztua. Datuak gordeta daude volume-an.
echo Dena ezabatzeko (BD barne): docker compose -f docker-compose.windows.yml down -v
echo.
pause