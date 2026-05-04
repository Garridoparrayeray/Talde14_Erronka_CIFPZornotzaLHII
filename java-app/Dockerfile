FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /java-app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN touch /.env && mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /java-app
RUN apt-get update && apt-get install -y \
    libxext6 libxrender1 libxtst6 libxi6 \
    libgl1 libgl1-mesa-dri \
    libgtk-3-0 libgdk-pixbuf2.0-0 \
    libpango-1.0-0 libatk1.0-0 libcairo2 \
    libfreetype6 libfontconfig1 \
    libxxf86vm1 libxrandr2 \
    && rm -rf /var/lib/apt/lists/*
ENV LIBGL_ALWAYS_SOFTWARE=1
COPY --from=builder /java-app/target/galdutakoak-1.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
