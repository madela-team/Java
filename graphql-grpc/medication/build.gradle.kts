plugins {
    // Подключает поддержку Spring Boot и его механизма автоконфигурации
    id("org.springframework.boot") version "3.0.9"

    // Позволяет использовать BOM зависимости Spring Cloud
    id("io.spring.dependency-management") version "1.1.0"

    // Подключение Liquibase — инструмент миграции схемы БД
    id("org.liquibase.gradle") version "2.2.0"

    id("java")

    // Плагин для генерации Java-кода из .proto файлов
    id("com.google.protobuf") version "0.9.4"
}

group = "dev.madela.apteka"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    // Базовые зависимости Spring Boot
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator") // метрики и мониторинг

    // Поддержка gRPC
    implementation("io.grpc:grpc-netty-shaded:1.63.0") // транспорт gRPC
    implementation("io.grpc:grpc-stub:1.63.0")         // клиентская заглушка
    implementation("io.grpc:grpc-protobuf:1.63.0")     // protobuf-кодеки
    implementation("com.google.protobuf:protobuf-java:3.25.3")

    // gRPC Spring Boot Starter — автоконфигурация gRPC серверов с аннотацией @GrpcService
    implementation("net.devh:grpc-server-spring-boot-starter:2.15.0.RELEASE")

    // Spring Cloud Consul Discovery — регистрация и поиск сервисов через Consul
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")

    // JDBC-драйвер PostgreSQL + Spring Boot JDBC Starter
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    runtimeOnly("org.postgresql:postgresql")

    // Liquibase — миграции БД
    implementation("org.liquibase:liquibase-core")

    // Мониторинг и метрики в формате Prometheus
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Для генерации кода, требующего аннотацию @Generated
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    // Тестирование
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    // Подключение BOM для Spring Cloud — фиксирует версии зависимостей в рамках одного набора
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.4")
    }
}

protobuf {
    protoc {
        // Указываем версию protoc — основной генератор Java-классов из .proto-файлов
        artifact = "com.google.protobuf:protoc:3.25.3"
    }
    plugins {
        // gRPC-плагин для protoc — генерирует интерфейсы gRPC-сервисов
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.63.0"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("grpc") // активируем плагин генерации gRPC-клиентов/серверов
            }
        }
    }
}

sourceSets {
    main {
        proto {
            // Каталог с .proto-файлами
            srcDir("proto")
        }
        java {
            // Каталоги с сгенерированными .java-файлами по protobuf и gRPC
            srcDir("build/generated/source/proto/main/java")
            srcDir("build/generated/source/proto/main/grpc")
        }
    }
}
