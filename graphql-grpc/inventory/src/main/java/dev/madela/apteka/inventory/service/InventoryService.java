package dev.madela.apteka.inventory.service;

import dev.madela.apteka.inventory.model.PharmacyStock;
import dev.madela.apteka.inventory.repository.PharmacyStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final PharmacyStockRepository repository;

    /**
     * Получить список остатков по идентификатору медикамента.
     */
    public List<PharmacyStock> getStock(String medicationId) {
        return repository.findByMedicationId(medicationId);
    }

    /**
     * Обновить остатки для заданной аптеки.
     * Если запись уже существует — обновляется количество.
     * Если записи нет — создаётся новая.
     */
    public List<PharmacyStock> updateStock(String pharmacyId, List<PharmacyStock> updates) {
        for (PharmacyStock update : updates) {
            // Поиск существующей записи по medicationId и pharmacyId
            var existing = repository.findByMedicationId(update.getMedicationId()).stream()
                    .filter(stock -> stock.getPharmacyId().equals(pharmacyId))
                    .findFirst();

            if (existing.isPresent()) {
                // Обновляем количество
                PharmacyStock stock = existing.get();
                stock.setQuantity(stock.getQuantity() + update.getQuantity());
                repository.save(stock);
            } else {
                // Создаём новую запись
                repository.save(PharmacyStock.builder()
                        .pharmacyId(pharmacyId)
                        .medicationId(update.getMedicationId())
                        .quantity(update.getQuantity())
                        .expiryDate(update.getExpiryDate())
                        .build());
            }
        }

        // Возвращаем обновлённый список для первого медикамента
        return repository.findByMedicationId(updates.get(0).getMedicationId());
    }

    /**
     * Получить список медикаментов с истёкшим сроком годности.
     *
     * Примечание:
     * Используется in-memory фильтрация, т.к. в MongoRepository нет метода по expiryDate.
     * Для больших объёмов данных желательно реализовать через @Query.
     */
    public List<PharmacyStock> getExpiredMedications(String expiryDate) {
        return repository.findAll().stream()
                .filter(stock -> stock.getExpiryDate().compareTo(expiryDate) <= 0)
                .toList();
    }

    /**
     * Удалить все записи по медикаменту.
     */
    public boolean deleteByMedicationId(String medicationId) {
        repository.deleteByMedicationId(medicationId);
        return true;
    }
}
