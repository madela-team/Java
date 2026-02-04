# ---------- BUILD ----------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

COPY reminder-service/pom.xml pom.xml
COPY reminder-service/src src

# Ретраи + чистка битых файлов в ~/.m2
RUN set -eux; \
    for i in 1 2 3 4 5; do \
      mvn -B -DskipTests package \
        -Dmaven.wagon.http.retryHandler.count=6 \
        -Dmaven.wagon.http.retryHandler.requestSentEnabled=true \
        -Dmaven.wagon.httpconnectionManager.ttlSeconds=60 \
      && break; \
      echo "Maven download failed, retry $i/5... cleaning partial downloads"; \
      find /root/.m2 -name "*.lastUpdated" -delete || true; \
      find /root/.m2 -name "*.part" -delete || true; \
      sleep 2; \
    done

# ---------- RUNTIME ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java","-jar","app.jar"]
