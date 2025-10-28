package dev.madela.apteka.scalars;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Кастомный GraphQL-скаляр для поддержки java.time.LocalDateTime.
 *
 * Используется в GraphQL-схемах через:
 *   scalar LocalDateTime
 *
 * Ожидаемый формат: "yyyy-MM-dd HH:mm"
 *
 * Автор: MaDeLa
 */
@DgsScalar(name = "LocalDateTime")
public class LocalDateTimeScalar implements Coercing<LocalDateTime, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Сериализация Java-объекта (LocalDateTime) в строку для отправки клиенту.
     */
    @Override
    public String serialize(Object dataFetcherResult) {
        if (dataFetcherResult instanceof LocalDateTime dateTime) {
            return FORMATTER.format(dateTime);
        }
        throw new CoercingSerializeException("Expected a LocalDateTime object.");
    }

    /**
     * Парсинг входного значения от клиента (например, в аргументах мутации).
     */
    @Override
    public LocalDateTime parseValue(Object input) {
        if (input instanceof String dateTimeStr) {
            try {
                return LocalDateTime.parse(dateTimeStr, FORMATTER);
            } catch (DateTimeParseException e) {
                throw new CoercingParseValueException(
                        "Invalid LocalDateTime format. Expected 'yyyy-MM-dd HH:mm'.", e);
            }
        }
        throw new CoercingParseValueException("Expected a String value for LocalDateTime.");
    }

    /**
     * Парсинг литерала в GraphQL-запросе (например, в raw-запросе без переменных).
     */
    @Override
    public LocalDateTime parseLiteral(Object input) {
        if (input instanceof StringValue stringValue) {
            try {
                return LocalDateTime.parse(stringValue.getValue(), FORMATTER);
            } catch (DateTimeParseException e) {
                throw new CoercingParseLiteralException(
                        "Invalid LocalDateTime literal. Expected 'yyyy-MM-dd HH:mm'.", e);
            }
        }
        throw new CoercingParseLiteralException("Expected a StringValue for LocalDateTime literal.");
    }
}

/***
 * Что такое Coercing<TJava, TGraphQL>?
 *
 * Интерфейс Coercing определяет правила преобразования между:
 *
 *     Java-объектом (TJava) — то, с чем работает сервер
 *
 *     GraphQL-представлением (TGraphQL) — то, что передаёт или получает клиент
 *
 * Метод	Назначение
 * serialize()	Преобразует Java-объект в строку (от сервера к клиенту)
 * parseValue()	Преобразует входное значение от клиента в Java (например, переменные в мутации)
 * parseLiteral()	Преобразует литерал, написанный напрямую в теле запроса
 * Где это используется в проекте:
 *
 *     GraphQL-схема объявляет: scalar LocalDateTime
 *
 *     Конфигурация GraphQLScalarConfiguration регистрирует этот класс как обработчик типа
 *
 *     В @InputArgument можно использовать LocalDateTime, и всё будет корректно парситься
 *
 *     Пример запроса:
 *     mutation {
 *      updateMedicationPrice(id: "123", newPrice: 55.0) {
 *          id
 *          newPrice
 *          updatedAt  # ← может быть типа LocalDateTime
 *      }
 *    }
 */