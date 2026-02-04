# ---------- BUILD ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /build

# Сначала pom (кэш зависимостей)
COPY user-service/pom.xml pom.xml
RUN mvn -B -q dependency:go-offline

# Потом исходники
COPY user-service/src src
RUN mvn -B -q -DskipTests package

# ---------- RUNTIME ----------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=build /build/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
