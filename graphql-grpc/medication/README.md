# medication-service

Сервис управления лекарствами.

## Основной функционал 
- Хранение информации о лекарствах
- Поиск и фильтрация с пагинацией и сортировкой
- Обновление цены лекарства
- Удаление лекарства с каскадными действиями (gRPC-запрос в inventory)

## Технологии
- Java 17
- Spring Boot 3.*
- gRPC (Netty)
- Spring JDBC
- Liquibase (PostgreSQL)
- Consul (Service Discovery)
- Docker

## Запуск
```bash
./gradlew bootRun

medication/
├── build.gradle.kts
├── settings.gradle.kts
├── proto/
│   └── medication-service.proto
├── src/
│   └── main/
│       ├── java/dev/madela/apteka/medication/...
│       ├── resources/
│       │   ├── application.yaml
│       │   └── db/changelog/db.changelog-master.yaml
└── README.md