plugins {
    // Плагин Spring Boot — управляет сборкой, запуском и паковкой приложения
    id("org.springframework.boot") version "3.0.9"

    // Управление зависимостями Spring (BOM-стиль)
    id("io.spring.dependency-management") version "1.1.0"

    // Плагин для миграций базы данных (используется Flyway)
    id("org.flywaydb.flyway") version "9.22.3"

    // Подключение Java как основного языка
    id("java")

    // Плагин для генерации gRPC/Protobuf-кода
    id("com.google.protobuf") version "0.9.4"
}

group = "dev.madela.apteka"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17  // Устанавливаем версию Java 17

repositories {
    mavenCentral()  // Центральный репозиторий Maven для всех зависимостей
}

dependencies {
    // Базовые стартеры Spring Boot
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator") // метрики и эндпоинты

    // --- gRPC ---
    implementation("io.grpc:grpc-netty-shaded:1.63.0")       // Транспортный слой gRPC (Netty)
    implementation("io.grpc:grpc-stub:1.63.0")                // Генерация клиентских и серверных обёрток
    implementation("io.grpc:grpc-protobuf:1.63.0")            // Поддержка protobuf в gRPC
    implementation("com.google.protobuf:protobuf-java:3.25.3")// Библиотека работы с protobuf

    // Spring Boot интеграция с gRPC (аннотация @GrpcService)
    implementation("net.devh:grpc-server-spring-boot-starter:2.15.0.RELEASE")

    // --- Service Discovery ---
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")
    // Позволяет регистрировать сервис в Consul и обнаруживать другие сервисы

    // --- Доступ к БД ---
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")  // JDBC-драйвер для PostgreSQL
    implementation("org.flywaydb:flyway-core") // миграции базы данных

    // --- Lombok ---
    compileOnly("org.projectlombok:lombok")             // только на этапе компиляции
    annotationProcessor("org.projectlombok:lombok")     // генерация кода (геттеры, билдеры и пр.)

    // --- Для поддержки @Generated ---
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    // --- Тестирование ---
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        // Подключение BOM Spring Cloud для Consul и других cloud-зависимостей
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.4")
    }
}

// --- Конфигурация генерации gRPC и Protobuf-кода ---
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.3"  // версия компилятора protobuf
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.63.0" // плагин для генерации gRPC-классов
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")  // включаем генерацию gRPC-классов для всех proto-файлов
            }
        }
    }
}

// --- Указание путей к proto-файлам и сгенерированным классам ---
sourceSets {
    main {
        proto {
            srcDir("proto")  // где лежат .proto файлы
        }
        java {
            // Подключаем каталоги с автогенерированным кодом
            srcDir("build/generated/source/proto/main/java")
            srcDir("build/generated/source/proto/main/grpc")
        }
    }
}
