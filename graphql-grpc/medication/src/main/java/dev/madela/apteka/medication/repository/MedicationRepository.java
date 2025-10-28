package dev.madela.apteka.medication.repository;

import dev.madela.apteka.medication.model.CategoryStock;
import dev.madela.apteka.medication.model.Medication;
import dev.madela.apteka.medication.model.PharmacyStatistics;
import dev.madela.apteka.medication.repository.mapper.MedicationRowMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

/**
 * Репозиторий для работы с таблицей `medication`.
 * <p>
 * Выполняет выборки, обновления, soft delete, фильтрации и расчёты по аптекам.
 * Использует {@link JdbcTemplate} и кастомный {@link MedicationRowMapper}.
 * <p>
 * Репозиторий ориентирован на точечную работу с SQL (без JPA).
 * <p>
 * Автор: MaDeLa
 */
@Repository
@RequiredArgsConstructor
public class MedicationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Medication> rowMapper = new MedicationRowMapper();

    /**
     * Получение всех медикаментов (без фильтрации).
     */
    public List<Medication> findAll() {
        String sql = "SELECT * FROM medication";
        return jdbcTemplate.query(sql, rowMapper);
    }

    /**
     * Мягкое удаление (soft delete) медикамента по ID.
     * Просто помечает запись как удалённую, не удаляя её физически.
     */
    public void softDeleteById(UUID id) {
        String sql = "UPDATE medication SET is_deleted = TRUE WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Мягкое удаление просроченных медикаментов (expiry_date <= переданная дата).
     * Возвращает список ID удалённых записей.
     */
    public List<UUID> softDeleteExpired(LocalDate expiryDate) {
        String sqlSelect = "SELECT id FROM medication WHERE expiry_date <= ? AND is_deleted = FALSE";
        List<UUID> ids = jdbcTemplate.queryForList(sqlSelect, UUID.class, expiryDate);

        if (!ids.isEmpty()) {
            // Используется именованный параметр (:ids) для удобного IN-запроса
            String sqlUpdate = "UPDATE medication SET is_deleted = TRUE WHERE id IN (:ids)";
            Map<String, Object> params = Map.of("ids", ids);
            NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
            namedTemplate.update(sqlUpdate, params);
        }

        return ids;
    }

    /**
     * Обновление цены медикамента.
     */
    public void updatePrice(UUID id, double newPrice) {
        String sql = "UPDATE medication SET price = ? WHERE id = ?";
        jdbcTemplate.update(sql, newPrice, id);
    }

    /**
     * Фильтрация медикаментов по категории, цене и пагинации.
     *
     * @param category фильтр по категории
     * @param minPrice нижний предел цены
     * @param maxPrice верхний предел цены
     * @param page     номер страницы (0-based)
     * @param size     количество элементов на странице
     */
    public List<Medication> findFiltered(String category, Double minPrice, Double maxPrice, int page, int size) {
        var sql = new StringBuilder("SELECT * FROM medication WHERE 1=1");
        var params = new ArrayList<>();

        if (category != null) {
            sql.append(" AND category = ?");
            params.add(category);
        }
        if (minPrice != null) {
            sql.append(" AND price >= ?");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND price <= ?");
            params.add(maxPrice);
        }

        sql.append(" ORDER BY name ASC LIMIT ? OFFSET ?");
        params.add(size);
        params.add(page * size);

        return jdbcTemplate.query(sql.toString(), params.toArray(), rowMapper);
    }

    /**
     * Подсчёт количества медикаментов, подходящих под фильтр.
     */
    public int countFiltered(String category, Double minPrice, Double maxPrice) {
        var sql = new StringBuilder("SELECT COUNT(*) FROM medication WHERE 1=1");
        var params = new ArrayList<Object>();

        if (category != null) {
            sql.append(" AND category = ?");
            params.add(category);
        }
        if (minPrice != null) {
            sql.append(" AND price >= ?");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND price <= ?");
            params.add(maxPrice);
        }

        return jdbcTemplate.queryForObject(sql.toString(), params.toArray(), Integer.class);
    }

    /**
     * Получение медикамента по строковому UUID.
     */
    public Medication findById(String id) {
        return findById(UUID.fromString(id));
    }

    /**
     * Получение медикамента по UUID.
     */
    public Medication findById(UUID id) {
        var sql = "SELECT * FROM medication WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Жёсткое удаление медикамента (физическое удаление из БД).
     * Используется только в особых случаях.
     */
    public void deleteById(String id) {
        jdbcTemplate.update("DELETE FROM medication WHERE id = ?", id);
    }

    /**
     * Получение списка медикаментов по списку строковых UUID.
     * Применяется фильтрация по `is_deleted = false`.
     */
    public List<Medication> findByIds(List<String> idsList) {
        if (CollectionUtils.isEmpty(idsList)) {
            return List.of();
        }

        List<UUID> uuidList = idsList.stream().map(UUID::fromString).toList();
        String inSql = String.join(",", Collections.nCopies(uuidList.size(), "?"));
        String sql = String.format("SELECT * FROM medication WHERE id IN (%s) AND is_deleted = FALSE", inSql);

        return jdbcTemplate.query(sql, rowMapper, uuidList.toArray());
    }

    /**
     * Получение медикаментов, принадлежащих конкретной аптеке.
     */
    public List<Medication> findByPharmacyId(UUID pharmacyId) {
        String sql = "SELECT * FROM medication WHERE pharmacy_id = ? AND is_deleted = FALSE";
        return jdbcTemplate.query(sql, rowMapper, pharmacyId);
    }

    /**
     * Вычисление статистики по аптеке:
     * - общее количество медикаментов
     * - средняя цена
     * - разбивка по категориям (category → count)
     */
    public PharmacyStatistics computePharmacyStatistics(UUID pharmacyId) {
        String countSql = "SELECT COUNT(*) FROM medication WHERE pharmacy_id = ? AND is_deleted = FALSE";
        String avgSql = "SELECT AVG(price) FROM medication WHERE pharmacy_id = ? AND is_deleted = FALSE";
        String categorySql = "SELECT category, COUNT(*) FROM medication WHERE pharmacy_id = ? AND is_deleted = FALSE GROUP BY category";

        int totalMedications = jdbcTemplate.queryForObject(countSql, Integer.class, pharmacyId);
        double averagePrice = jdbcTemplate.queryForObject(avgSql, Double.class, pharmacyId);

        List<CategoryStock> stockByCategory = jdbcTemplate.query(categorySql, (rs, rowNum) ->
                new CategoryStock(rs.getString("category"), rs.getInt("count")), pharmacyId);

        return PharmacyStatistics.builder()
                .totalMedications(totalMedications)
                .averagePrice(averagePrice)
                .stockByCategory(stockByCategory)
                .build();
    }
}
