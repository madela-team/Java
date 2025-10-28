plugins {
    // Подключение плагина Spring Boot (обеспечивает сборку, запуск, и автоконфигурацию)
    id("org.springframework.boot") version "3.0.9"

    // Управление зависимостями по spring-стилю
    id("io.spring.dependency-management") version "1.1.0"

    // Поддержка генерации gRPC-кода из .proto-файлов
    id("com.google.protobuf") version "0.9.4"

    // Базовый плагин Java
    id("java")
}

group = "dev.madela.apteka"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

// Версия Spring Cloud BOM для автоконфигурации discovery и других компонентов
extra["springCloudVersion"] = "2022.0.4"

repositories {
    mavenCentral()
    maven("https://repo.spring.io/release") // нужен для spring-cloud
}

dependencies {
    // Стандартный spring boot, без web — т.к. REST здесь не используется
    implementation("org.springframework.boot:spring-boot-starter")

    // Используем MongoDB как хранилище остатков
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // gRPC зависимости
    implementation("io.grpc:grpc-netty-shaded:1.63.0")        // транспорт gRPC
    implementation("io.grpc:grpc-protobuf:1.63.0")            // поддержка protobuf
    implementation("io.grpc:grpc-stub:1.63.0")                // клиентские и серверные заглушки
    implementation("com.google.protobuf:protobuf-java:3.25.3") // сама библиотека protobuf

    // Spring Boot интеграция для gRPC: позволяет использовать @GrpcService
    implementation("net.devh:grpc-server-spring-boot-starter:2.15.0.RELEASE")

    // Метрики и мониторинг
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Service Discovery через Consul
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")

    // Lombok — подключается как compileOnly, чтобы не попадать в финальный .jar
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Для аннотации @Generated, чтобы не ругались инструменты анализа
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    // Зависимости для тестов
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        // Импортируем BOM Spring Cloud — так проще следить за совместимостью версий
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

protobuf {
    protoc {
        // Указываем используемый компилятор .proto файлов
        artifact = "com.google.protobuf:protoc:3.25.3"
    }
    plugins {
        create("grpc") {
            // Указываем плагин генерации gRPC-кода
            artifact = "io.grpc:protoc-gen-grpc-java:1.63.0"
        }
    }
    generateProtoTasks {
        all().forEach {
            // Для всех proto-задач подключаем gRPC-плагин
            it.plugins {
                create("grpc")
            }
        }
    }
}

sourceSets {
    main {
        proto {
            // Указываем папку, где лежат .proto-файлы
            srcDir("proto")
        }
        java {
            // Указываем, откуда будут браться сгенерированные классы
            srcDirs("build/generated/source/proto/main/java",
                "build/generated/source/proto/main/grpc")
        }
    }
}
