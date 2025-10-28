# --- Stage 1: Build ---
FROM gradle:8.5-jdk17 AS builder

# Настройки окружения
ENV GRADLE_USER_HOME=/home/gradle/.gradle \
    SPRING_PROFILES_ACTIVE=docker

# Рабочая директория внутри контейнера
WORKDIR /app

# Сначала копируем только файлы сборки, чтобы кэшировались зависимости
COPY build.gradle.kts settings.gradle.kts /app/

# Копируем остальной код (src, ресурсы и т.д.)
COPY . /app

# Можно управлять "чистой" сборкой через аргумент (по умолчанию true)
ARG CLEAN_BUILD=true

# Сборка проекта без демона Gradle (удобно для CI/CD)
RUN if [ "$CLEAN_BUILD" = "true" ]; then \
      gradle clean build --no-daemon; \
    else \
      gradle build --no-daemon; \
    fi

# --- Stage 2: Runtime ---
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Копируем скомпилированный jar-файл
COPY --from=builder /app/build/libs/*.jar app.jar

# Порт Spring Boot по умолчанию
EXPOSE 8080

# Точка входа — запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]