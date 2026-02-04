FROM maven:3.6.3-jdk-8 AS build

WORKDIR /app

# Копируем pom модуля и его исходники
COPY api-gateway/pom.xml ./
COPY api-gateway/src ./src

RUN mvn -B -DskipTests clean package

FROM eclipse-temurin:8-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
