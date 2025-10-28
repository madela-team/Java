package dev.madela.apteka.medication.service;

import dev.madela.apteka.medication.model.Medication;
import dev.madela.apteka.medication.model.PharmacyStatistics;
import dev.madela.apteka.medication.repository.ActiveSubstanceRepository;
import dev.madela.apteka.medication.repository.MedicationRepository;
import dev.madela.apteka.medication.repository.PriceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Сервисный слой для работы с медикаментами.
 * <p>
 * Отвечает за объединение данных из разных источников:
 * - {@link MedicationRepository} — основная информация о препаратах
 * - {@link ActiveSubstanceRepository} — активные вещества
 * - {@link PriceHistoryRepository} — история изменений цен
 * <p>
 * Реализует бизнес-логику, недоступную напрямую в репозиториях.
 * <p>
 * Автор: MaDeLa
 */
@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final ActiveSubstanceRepository activeSubstanceRepository;
    private final PriceHistoryRepository priceHistoryRepository;

    /**
     * Получает все медикаменты, включая активные вещества.
     */
    public List<Medication> getAllMedications() {
        var medications = medicationRepository.findAll();

        // Для каждого медикамента загружаем связанные активные вещества
        medications.forEach(med ->
                med.setActiveSubstances(activeSubstanceRepository.findByMedicationId(med.getId()))
        );

        return medications;
    }

    /**
     * Получение одного медикамента по UUID с активными веществами.
     */
    public Medication getById(UUID id) {
        var medication = medicationRepository.findById(id);
        if (medication != null) {
            medication.setActiveSubstances(activeSubstanceRepository.findByMedicationId(id));
        }
        return medication;
    }

    /**
     * Получение списка медикаментов с фильтрацией и пагинацией.
     * Также подтягивает активные вещества.
     */
    public List<Medication> getFilteredMedications(String category, Double minPrice, Double maxPrice, int page, int size) {
        var medications = medicationRepository.findFiltered(category, minPrice, maxPrice, page, size);

        medications.forEach(med ->
                med.setActiveSubstances(activeSubstanceRepository.findByMedicationId(med.getId()))
        );

        return medications;
    }

    /**
     * Подсчёт количества медикаментов, подходящих под заданные фильтры.
     */
    public int getFilteredCount(String category, Double minPrice, Double maxPrice) {
        return medicationRepository.countFiltered(category, minPrice, maxPrice);
    }

    /**
     * Обновляет цену у медикамента и сохраняет историю изменения.
     *
     * @param medicationId UUID медикамента
     * @param newPrice     новая цена
     * @return обновлённый объект {@link Medication}
     */
    public Medication updatePrice(UUID medicationId, double newPrice) {
        var current = medicationRepository.findById(medicationId);
        double oldPrice = current.getPrice();

        medicationRepository.updatePrice(medicationId, newPrice);
        priceHistoryRepository.saveChange(medicationId, oldPrice, newPrice); // логируем изменение

        current.setPrice(newPrice);
        return current;
    }

    /**
     * Мягкое удаление (soft delete) медикамента по ID.
     */
    public void deleteById(UUID id) {
        medicationRepository.softDeleteById(id);
    }

    /**
     * Удаление (soft delete) медикаментов, срок годности которых истёк до указанной даты.
     *
     * @param expiryDate дата истечения (включительно)
     * @return список ID удалённых медикаментов
     */
    public List<UUID> deleteExpiredMedications(LocalDate expiryDate) {
        return medicationRepository.softDeleteExpired(expiryDate);
    }

    /**
     * Получение медикамента по строковому UUID (для совместимости).
     */
    public Medication findById(String id) {
        return medicationRepository.findById(id);
    }

    /**
     * Получение списка медикаментов по списку строковых UUID.
     */
    public List<Medication> findByIds(List<String> idsList) {
        return medicationRepository.findByIds(idsList);
    }

    /**
     * Получение всех медикаментов, принадлежащих указанной аптеке.
     */
    public List<Medication> findByPharmacyId(UUID pharmacyId) {
        return medicationRepository.findByPharmacyId(pharmacyId);
    }

    /**
     * Получение сводной статистики по аптеке:
     * - количество препаратов
     * - средняя цена
     * - разбивка по категориям
     */
    public PharmacyStatistics getPharmacyStatistics(UUID pharmacyId) {
        return medicationRepository.computePharmacyStatistics(pharmacyId);
    }
}
