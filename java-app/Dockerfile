# 1. ETAPA: Konpilazioa Maven-ekin
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . /app
WORKDIR /app
# JavaFX liburutegiak eta aplikazioa paketatu
RUN mvn clean package -DskipTests
# 2. ETAPA: Exekuzioa
FROM eclipse-temurin:21-jre

# Windows-en (WSL2) eta Linux-en grafikoak kudeatzeko beharrezkoak
RUN apt-get update && apt-get install -y \
    libgtk-3-0 \
    libglu1-mesa \
    libgl1-mesa-dri \
    libgl1 \
    libglx0 \
    libpangocairo-1.0-0 \
    libatk1.0-0 \
    libgdk-pixbuf2.0-0 \
    libcanberra-gtk3-module \
    xvfb \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar /app/app.jar
WORKDIR /app

ENV JAVA_OPTS="-Djdk.gtk.version=3"

CMD ["java", "-jar", "app.jar"]