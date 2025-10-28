package dev.madela.apteka.medication.repository.mapper;

import dev.madela.apteka.medication.model.ActiveSubstance;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Маппер для преобразования строки результата SQL-запроса (ResultSet)
 * в объект {@link ActiveSubstance}.
 * <p>
 * Используется в репозиториях, работающих с таблицей {@code active_substance}.
 * <p>
 * Автор: MaDeLa
 */
public class ActiveSubstanceRowMapper implements RowMapper<ActiveSubstance> {

    /**
     * Преобразует текущую строку {@link ResultSet} в объект {@link ActiveSubstance}.
     *
     * @param rs      результат SQL-запроса
     * @param rowNum  номер строки (не используется, но передаётся)
     * @return объект {@link ActiveSubstance}, собранный из полей строки
     * @throws SQLException в случае ошибок чтения данных из ResultSet
     */
    @Override
    public ActiveSubstance mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ActiveSubstance.builder()
                .id(UUID.fromString(rs.getString("id")))                      // ID записи
                .medicationId(UUID.fromString(rs.getString("medication_id"))) // ID медикамента (внешний ключ)
                .name(rs.getString("name"))                                   // Название действующего вещества
                .concentration(rs.getDouble("concentration"))                 // Концентрация вещества
                .build();
    }
}
