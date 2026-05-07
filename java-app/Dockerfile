FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:resolve dependency:resolve-plugins
COPY src ./src
RUN mvn clean package -DskipTests

FROM ubuntu:22.04
WORKDIR /app

ENV DEBIAN_FRONTEND=noninteractive TZ=UTC

RUN apt-get update && apt-get install -y \
    openjdk-21-jdk \
    xvfb x11-utils x11vnc \
    novnc websockify \
    dbus-x11 \
    libgtk-3-0 libgdk-pixbuf2.0-0 \
    libx11-6 libxext6 libxrender1 libxtst6 libxi6 libgl1 \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/target/galdutakoak-1.0-SNAPSHOT.jar app.jar
COPY start.sh .
RUN sed -i 's/\r$//' start.sh && chmod +x start.sh

EXPOSE 5900 6080

CMD ["./start.sh"]
