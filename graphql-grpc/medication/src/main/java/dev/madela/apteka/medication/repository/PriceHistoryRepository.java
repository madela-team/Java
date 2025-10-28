package dev.madela.apteka.medication.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для сохранения истории изменения цен на медикаменты.
 * <p>
 * Используется для аудита — хранит старую и новую цену при обновлении.
 * Каждое изменение записывается как отдельная строка в таблицу {@code medication_price_history}.
 * <p>
 * Автор: MaDeLa
 */
@Repository
@RequiredArgsConstructor
public class PriceHistoryRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Сохраняет информацию об изменении цены медикамента.
     *
     * @param medicationId ID медикамента, цена которого была изменена
     * @param oldPrice     старая цена (до изменения)
     * @param newPrice     новая цена (после изменения)
     *
     * Таблица {@code medication_price_history} должна иметь следующие поля:
     * - id (UUID, PK)
     * - medication_id (UUID, FK)
     * - old_price (DOUBLE)
     * - new_price (DOUBLE)
     * - created_at (TIMESTAMP, по умолчанию NOW())
     */
    public void saveChange(UUID medicationId, double oldPrice, double newPrice) {
        jdbcTemplate.update(
                "INSERT INTO medication_price_history (id, medication_id, old_price, new_price) VALUES (?, ?, ?, ?)",
                UUID.randomUUID(),  // Генерация ID для записи истории
                medicationId,
                oldPrice,
                newPrice
        );
    }
}
