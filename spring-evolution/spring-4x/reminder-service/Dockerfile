# ---------- BUILD ----------
FROM maven:3.6.3-jdk-8 AS build

WORKDIR /app

# Копируем pom и исходники
COPY reminder-service/pom.xml ./
COPY reminder-service/src ./src

# Сборка fat-jar
RUN mvn -DskipTests package

# ---------- RUNTIME ----------
FROM eclipse-temurin:8-jre-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
