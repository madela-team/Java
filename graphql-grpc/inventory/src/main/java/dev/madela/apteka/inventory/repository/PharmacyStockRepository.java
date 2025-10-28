package dev.madela.apteka.inventory.repository;

import dev.madela.apteka.inventory.model.PharmacyStock;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Репозиторий для работы с коллекцией {@code pharmacy_stock} в MongoDB.
 * <p>
 * Наследуется от {@link MongoRepository}, который автоматически реализует CRUD-операции
 * и предоставляет поддержку Spring Data MongoDB.
 *
 * MongoRepository<PharmacyStock, String>:
 *   - первый параметр: тип сущности
 *   - второй параметр: тип ID поля (@Id), обычно String или ObjectId
 */
public interface PharmacyStockRepository extends MongoRepository<PharmacyStock, String> {

    /**
     * Поиск всех записей по идентификатору медикамента.
     * Spring Data автоматически реализует метод на основе имени.
     */
    List<PharmacyStock> findByMedicationId(String medicationId);

    /**
     * Удаление всех записей по идентификатору медикамента.
     */
    void deleteByMedicationId(String medicationId);
}
