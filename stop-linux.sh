#!/bin/bash
echo "Edukiontziak geldiarazten..."
docker compose down
echo ""
echo "Geldiaraztua. Datuak gordeta daude volume-an."
echo "Dena ezabatzeko (BD barne): docker compose down -v"