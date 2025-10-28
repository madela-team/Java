Сборщик: Gradle (Kotlin DSL) – современный, гибкий, поддерживает Java 17 и реактивные стеки.

Стек:

* Spring Boot 3.2 (с поддержкой Java 17)
* GraphQL Java + DGS Framework (от Netflix)
* gRPC (для вызовов микросервисов)
* Lombok (уменьшение boilerplate-кода)

Примерная структура проекта:

gateway/  
├── proto/
│   ├── common.proto
│   ├── inventory-service.proto
│   ├── medication-service.proto
│   ├── order-service.proto
├── src/  
│   ├── main/  
│   │   ├── java/  
│   │   │   └── dev/madela/apteka/gateway/  
│   │   │       ├── config/                  # Конфиги  
│   │   │       │   ├── GrpcConfig.java      # Настройка gRPC клиентов  
│   │   │       │   └── GraphQLScalarConfiguration.java   # Конфигурация скаляров  
│   │   │       ├── controller/              # GraphQL-резолверы  
│   │   │       │   ├── MedicationResolver.java  
│   │   │       │   ├── OrderResolver.java  
│   │   │       │   └── InventoryResolver.java  
│   │   │       ├── model/                   # DTO (GraphQL-типы)  
│   │   │       │   ├── Medication.java  
│   │   │       │   ├── Order.java  
│   │   │       │   └── ...  
│   │   │       ├── scalars/                   # Кастомные GraphQL-типы  
│   │   │       │   ├── LocalDateScalar.java
│   │   │       │   ├── LocalDateTimeScalar.java
│   │   │       ├── service/                 # Логика вызовов gRPC  
│   │   │       │   ├── MedicationService.java  
│   │   │       │   ├── OrderService.java  
│   │   │       │   └── ...  
│   │   │       └── GatewayApplication.java  # Main-класс  
│   │   └── resources/  
│   │       ├── schema/                      # GraphQL-схемы  
│   │       │   ├── schema.graphqls
│   │       │   ├── medication.graphqls  
│   │       │   ├── order.graphqls  
│   │       │   └── inventory.graphqls
│   │       └── application.yml              # Настройки  
│   └── test/                                # Тесты  
├── build.gradle.kts                         # Gradle-конфиг  
└── settings.gradle.kts  