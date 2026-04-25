FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
# HEMOS CAMBIADO libgl1-mesa-glx POR libgl1
RUN apt-get update && apt-get install -y \
    libxext6 libxrender1 libxtst6 libxi6 libgl1 \
    && rm -rf /var/lib/apt/lists/*
COPY --from=builder /app/target/galdutakoak-1.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
