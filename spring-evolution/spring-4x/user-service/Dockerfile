# ---------- BUILD ----------
FROM maven:3.6.3-jdk-8 AS build

WORKDIR /app

# Копируем pom модуля и его исходники
COPY user-service/pom.xml ./
COPY user-service/src ./src

# Качаем зависимости и собираем
RUN mvn -DskipTests package

# ---------- RUNTIME ----------
FROM eclipse-temurin:8-jre-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]