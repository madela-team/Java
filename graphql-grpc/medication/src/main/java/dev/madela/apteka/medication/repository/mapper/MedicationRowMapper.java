package dev.madela.apteka.medication.repository.mapper;

import dev.madela.apteka.medication.model.Medication;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Маппер для преобразования строки результата SQL-запроса в объект {@link Medication}.
 * <p>
 * Используется в репозиториях при работе с таблицей {@code medication}.
 * <p>
 * Позволяет вручную контролировать преобразование полей, включая обработку дат и UUID.
 * <p>
 * Автор: MaDeLa
 */
public class MedicationRowMapper implements RowMapper<Medication> {

    /**
     * Преобразует строку {@link ResultSet} в объект {@link Medication}.
     *
     * @param rs     текущая строка из SQL-запроса
     * @param rowNum номер строки (для отладки, не используется напрямую)
     * @return объект {@link Medication}, собранный из данных строки
     * @throws SQLException в случае ошибок при чтении значений
     */
    @Override
    public Medication mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Medication.builder()
                .id(UUID.fromString(rs.getString("id"))) // ID медикамента (UUID)
                .name(rs.getString("name"))             // Название препарата
                .price(rs.getDouble("price"))           // Цена препарата
                .category(rs.getString("category"))     // Категория (например, "антибиотик", "витамин")
                .expiryDate(
                        rs.getDate("expiry_date") != null
                                ? rs.getDate("expiry_date").toLocalDate()
                                : null                  // Обработка возможного null в дате
                )
                .build();
    }
}
