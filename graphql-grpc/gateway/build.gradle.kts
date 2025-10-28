import com.google.protobuf.gradle.id

plugins {
    // Плагин Java для поддержки стандартных Java-функций сборки
    java
    // Подключение Spring Boot Gradle плагина — облегчает настройку и сборку приложений Spring Boot
    id("org.springframework.boot") version "3.4.5"
    // Управление зависимостями Spring (упрощает указание версий зависимостей)
    id("io.spring.dependency-management") version "1.1.7"
    // Плагин для генерации Java-кода на основе .proto-файлов (Protobuf/gRPC)
    id("com.google.protobuf") version "0.9.4"
}

// Общая информация о проекте
group = "com.example"
version = "0.0.1-SNAPSHOT"

// Конкретная группа для проекта MaDeLa
group = "dev.madela.apteka"
version = "0.0.1-SNAPSHOT"

// Указываем, что проект использует Java 17
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    // Подключение Maven Central как основного репозитория зависимостей
    mavenCentral()
}

// Версии внешних зависимостей вынесены в переменные
extra["netflixDgsVersion"] = "10.1.2" // Версия Netflix DGS для GraphQL
extra["springCloudVersion"] = "2024.0.1"
extra["springGrpcVersion"] = "0.8.0"

dependencies {
    // Основная зависимость для запуска GraphQL через DGS (от Netflix)
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter")

    // gRPC — поддержка сервисов на gRPC
    implementation("io.grpc:grpc-services")

    // Spring Cloud с интеграцией Consul для конфигураций и сервис-дискавери
    implementation("org.springframework.cloud:spring-cloud-starter-consul-config")
    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")

    // Интеграция Spring с gRPC
    implementation("org.springframework.grpc:spring-grpc-spring-boot-starter")

    // Web Starter — базовая зависимость для создания REST и веб-сервисов
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Подключение Prometheus через Micrometer для сбора метрик
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Lombok — удобный способ генерации геттеров, сеттеров и др. (только на этапе компиляции)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Для поддержки аннотации @Generated (некоторые инструменты и библиотеки её используют)
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    // Тестовые зависимости
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter-test")
    testImplementation("org.springframework.grpc:spring-grpc-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        // BOM-файл от Netflix для синхронизации всех версий DGS-зависимостей
        mavenBom("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:${property("netflixDgsVersion")}")
        // BOM для Spring gRPC
        mavenBom("org.springframework.grpc:spring-grpc-dependencies:${property("springGrpcVersion")}")
        // BOM для Spring Cloud (включает Consul и другие инструменты)
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

protobuf {
    protoc {
        // Указываем Protobuf компилятор
        artifact = "com.google.protobuf:protoc"
    }
    plugins {
        // Плагин для генерации gRPC-классов
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java"
        }
    }
    generateProtoTasks {
        // Применяем gRPC-плагин ко всем задачам генерации
        all().forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
}

sourceSets {
    main {
        proto {
            // Указываем путь к директории с .proto-файлами
            srcDir("proto")
        }
        java {
            // Добавляем директории с сгенерированным кодом из Protobuf и gRPC
            srcDir("build/generated/source/proto/main/java")
            srcDir("build/generated/source/proto/main/grpc")
        }
    }
}

tasks.test {
    doFirst {
        // Копирование схемы GraphQL в test-ресурсы перед тестами — нужно для корректного тестирования GraphQL-эндпоинтов
        copy {
            from("src/main/resources/schema")
            into("src/test/resources/schema")
        }
    }
}

tasks.withType<Test> {
    // Указание, что используется JUnit 5
    useJUnitPlatform()
}
