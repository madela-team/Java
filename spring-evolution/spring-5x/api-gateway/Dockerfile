# ---------- BUILD ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /build

COPY api-gateway/pom.xml pom.xml
RUN mvn -B -q dependency:go-offline

COPY api-gateway/src src
RUN mvn -B -q -DskipTests package

# ---------- RUNTIME ----------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=build /build/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
