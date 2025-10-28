# Order Service

Микросервис управления заказами в аптечной системе. Отвечает за создание, отмену и получение заказов пользователей.

## Технологии
- Java 17
- Spring Boot 3.2
- gRPC (Netty)
- JPA + Hibernate
- PostgreSQL
- Flyway
- Spring Cloud Consul (Service Discovery)
- Docker + Docker Compose

## ⚙Возможности
- Создание заказов с валидацией
- Отмена заказов
- Получение заказов пользователя (с пагинацией)
- Интеграция с gateway через gRPC
- Регистрация в Consul

## Структура
- `proto/` — gRPC контракты
- `src/main/java` — код микросервиса
- `src/main/resources/db/migration` — миграции Flyway
- `docker-compose.yml` — локальная среда

order-service/
├── build.gradle.kts
├── docker-compose.yml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── dev/madela/apteka/order/
│   │   │       ├── OrderServiceApplication.java
│   │   │       ├── entity/
│   │   │       │   ├── Order.java
│   │   │       │   └── OrderItem.java
│   │   │       ├── repository/
│   │   │       │   ├── OrderRepository.java
│   │   │       │   └── OrderItemRepository.java
│   │   │       ├── service/
│   │   │       │   └── OrderDomainService.java
│   │   │       ├── grpc/
│   │   │       │   └── OrderGrpcService.java
│   │   │       └── config/
│   │   │           └── ConsulConfig.java
│   │   ├── resources/
│   │   │   ├── application.yaml
│   │   │   └── db/migration/V1__init_orders.sql
└── proto/
└── order-service.proto
