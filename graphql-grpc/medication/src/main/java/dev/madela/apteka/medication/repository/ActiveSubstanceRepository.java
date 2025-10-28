package dev.madela.apteka.medication.repository;

import dev.madela.apteka.medication.repository.mapper.ActiveSubstanceRowMapper;
import dev.madela.apteka.medication.model.ActiveSubstance;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для получения активных веществ (действующих компонентов) медикаментов.
 * <p>
 * Использует {@link JdbcTemplate} для выполнения SQL-запросов вручную, без JPA/Hibernate.
 * Это позволяет точно контролировать SQL и работать с производительными запросами.
 * <p>
 * Автор: MaDeLa
 */
@Repository
@RequiredArgsConstructor
public class ActiveSubstanceRepository {

    /**
     * {@link JdbcTemplate} — компонент Spring, упрощающий выполнение SQL-запросов без ORM.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * Находит список активных веществ по ID медикамента.
     *
     * @param medicationId UUID медикамента, для которого нужно получить активные вещества
     * @return список {@link ActiveSubstance}, полученных из таблицы {@code active_substance}
     *
     * Пример SQL-запроса:
     * <pre>
     * SELECT * FROM active_substance WHERE medication_id = ?
     * </pre>
     *
     * Используется {@link ActiveSubstanceRowMapper} — отдельный класс, превращающий строки из ResultSet в Java-объекты.
     */
    public List<ActiveSubstance> findByMedicationId(UUID medicationId) {
        String sql = "SELECT * FROM active_substance WHERE medication_id = ?";

        // Выполняем SQL-запрос, передаём маппер и параметр
        return jdbcTemplate.query(sql, new ActiveSubstanceRowMapper(), medicationId);
    }
}
