package dev.madela.apteka.scalars;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Пользовательский скаляр GraphQL: LocalDate
 *
 * Этот класс позволяет корректно работать с java.time.LocalDate
 * в GraphQL-схеме через тип `scalar LocalDate`.
 *
 * Автор: MaDeLa
 */
@DgsScalar(name = "LocalDate")
public class LocalDateScalar implements Coercing<LocalDate, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Преобразование Java-объекта (LocalDate) в строку, которая будет отдана клиенту.
     */
    @Override
    public String serialize(Object dataFetcherResult) {
        if (dataFetcherResult instanceof LocalDate date) {
            return FORMATTER.format(date);
        }
        throw new CoercingSerializeException("Expected a LocalDate object.");
    }

    /**
     * Преобразование входного значения от клиента (например, из мутации) в LocalDate.
     */
    @Override
    public LocalDate parseValue(Object input) {
        if (input instanceof String dateStr) {
            try {
                return LocalDate.parse(dateStr, FORMATTER);
            } catch (DateTimeParseException e) {
                throw new CoercingParseValueException("Invalid LocalDate format. Expected 'yyyy-MM-dd'.", e);
            }
        }
        throw new CoercingParseValueException("Expected a String value for LocalDate.");
    }

    /**
     * Преобразование значения, переданного в GraphQL-запросе как литерал (напрямую в запросе).
     */
    @Override
    public LocalDate parseLiteral(Object input) {
        if (input instanceof StringValue stringValue) {
            try {
                return LocalDate.parse(stringValue.getValue(), FORMATTER);
            } catch (DateTimeParseException e) {
                throw new CoercingParseLiteralException("Invalid LocalDate literal. Expected 'yyyy-MM-dd'.", e);
            }
        }
        throw new CoercingParseLiteralException("Expected a StringValue for LocalDate literal.");
    }
}

/***
 * Что такое Coercing и зачем он нужен?
 *
 * Coercing<TInternal, TExternal> — это интерфейс из GraphQL Java, который используется для определения кастомных скалярных типов.
 * Метод	Назначение
 * serialize()	Как преобразовать Java-объект в значение для клиента (обычно строка)
 * parseValue()	Как преобразовать входное значение от клиента (например, в мутации)
 * parseLiteral()	Как распарсить значение, если оно задано как литерал в GraphQL-запросе
 *
 * Пример из GraphQL-запроса, где это используется:
 * mutation {
 *   deleteExpiredMedications(expiryDate: "2025-06-01") {
 *     count
 *   }
 * }
 *
 * expiryDate — скаляр LocalDate, в Java обрабатывается через parseLiteral() или parseValue().
 *
 * Важно помнить:
 *
 *     Если вы не реализуете Coercing, GraphQL не сможет понять, как обрабатывать нестандартные Java-типы (например, LocalDate, UUID, BigDecimal).
 *
 *     Без скаляров вы ограничены стандартными типами: Int, Float, Boolean, String, ID.
 */