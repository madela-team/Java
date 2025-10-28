package dev.madela.apteka.config;

import dev.madela.apteka.scalars.LocalDateScalar;
import dev.madela.apteka.scalars.LocalDateTimeScalar;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация пользовательских GraphQL скаляров.
 *
 * По умолчанию GraphQL не знает, как обрабатывать типы java.time.LocalDate и LocalDateTime.
 * Поэтому мы вручную регистрируем два кастомных скаляра, используя реализацию интерфейса Coercing.
 *
 * Эти скаляры также объявлены в schema.graphqls:
 * scalar LocalDate
 * scalar LocalDateTime
 *
 * !!! Обязательно нужно, чтобы названия в .graphqls и в Java-скалярах совпадали.
 *
 * Автор: MaDeLa
 */
@Configuration
public class GraphQLScalarConfiguration {

    /**
     * Регистрирует скаляр LocalDate.
     *
     * Название "LocalDate" должно точно совпадать с названием, использованным в .graphqls-файле:
     * scalar LocalDate
     *
     * Используется для сериализации и десериализации java.time.LocalDate (например, "2025-06-04").
     */
    @Bean
    public GraphQLScalarType localDateScalarType() {
        return GraphQLScalarType.newScalar()
                .name("LocalDate") // Название скаляра, как в GraphQL-схеме
                .description("A custom scalar that handles java.time.LocalDate")
                .coercing(new LocalDateScalar()) // Класс, реализующий преобразование между строкой и LocalDate
                .build();
    }

    /**
     * Регистрирует скаляр LocalDateTime.
     *
     * Название "LocalDateTime" также должно совпадать с тем, что указано в schema.graphqls:
     * scalar LocalDateTime
     *
     * Позволяет использовать тип java.time.LocalDateTime (например, "2025-06-04T12:30:00") в GraphQL.
     */
    @Bean
    public GraphQLScalarType localDateTimeScalarType() {
        return GraphQLScalarType.newScalar()
                .name("LocalDateTime")
                .description("A custom scalar that handles java.time.LocalDateTime")
                .coercing(new LocalDateTimeScalar())
                .build();
    }
}
