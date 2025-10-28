package dev.madela.apteka.service;

import dev.madela.apteka.model.*;
import dev.madela.apteka.proto.medication.MedicationServiceGrpc;
import dev.madela.apteka.proto.medication.MedicationsResponse;
import dev.madela.apteka.proto.medication.PharmacyMedicationsRequest;
import dev.madela.apteka.proto.medication.PharmacyMedicationsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис для работы с аптеками и связанными с ними данными о медикаментах.
 * Использует gRPC-клиент medication-сервиса для получения информации.
 *
 * Автор: MaDeLa
 */
@Service
@RequiredArgsConstructor
public class PharmacyService {

    private final MedicationServiceGrpc.MedicationServiceBlockingStub medicationGrpcClient;

    /**
     * Получает список медикаментов, сгруппированных по аптеке.
     * Используется для аналитики или вывода агрегированных данных на фронте.
     *
     * @param filter  фильтр по категории медикаментов
     * @param groupBy параметр группировки (зарезервирован, в текущей версии не используется)
     * @return список групп медикаментов по pharmacyId
     */
    public List<PharmacyMedicationGroup> getMedicationsGroupedByPharmacy(MedicationFilter filter, String groupBy) {
        // Вызов gRPC для получения списка медикаментов с указанным фильтром
        dev.madela.apteka.proto.medication.MedicationRequest grpcRequest = dev.madela.apteka.proto.medication.MedicationRequest.newBuilder()
                .setCategory(filter.getCategory() != null ? filter.getCategory() : "")
                .build();

        MedicationsResponse grpcResponse = medicationGrpcClient.getMedications(grpcRequest);

        // Группировка медикаментов по ID аптеки
        Map<String, List<Medication>> grouped = grpcResponse.getMedicationsList().stream()
                .map(this::mapToMedication)
                .collect(Collectors.groupingBy(Medication::getPharmacyId));

        // Преобразование сгруппированных данных в DTO-модель PharmacyMedicationGroup
        return grouped.entrySet().stream()
                .map(entry -> PharmacyMedicationGroup.builder()
                        .pharmacyId(entry.getKey())
                        .items(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Рассчитывает статистику по аптеке: общее количество медикаментов,
     * среднюю цену, распределение по категориям.
     *
     * @param pharmacyId идентификатор аптеки
     * @return объект PharmacyStatistics с агрегированными данными
     */
    public PharmacyStatistics getPharmacyStatistics(String pharmacyId) {
        // gRPC-запрос на получение медикаментов конкретной аптеки
        PharmacyMedicationsRequest grpcRequest = PharmacyMedicationsRequest.newBuilder()
                .setPharmacyId(pharmacyId)
                .build();

        PharmacyMedicationsResponse grpcResponse = medicationGrpcClient.getMedicationsByPharmacy(grpcRequest);

        // Маппим медикаменты в локальные DTO
        List<Medication> medications = grpcResponse.getMedicationsList().stream()
                .map(this::mapToMedication)
                .toList();

        // Подсчёт количества медикаментов
        int totalMedications = medications.size();

        // Расчёт средней цены
        double averagePrice = medications.stream()
                .mapToDouble(Medication::getPrice)
                .average()
                .orElse(0);

        // Группировка медикаментов по категориям с подсчётом количества
        Map<String, Long> stockByCategory = medications.stream()
                .collect(Collectors.groupingBy(Medication::getCategory, Collectors.counting()));

        // Преобразуем статистику по категориям в список DTO
        List<CategoryStock> stockList = stockByCategory.entrySet().stream()
                .map(e -> new CategoryStock(e.getKey(), e.getValue().intValue()))
                .collect(Collectors.toList());

        // Формируем объект ответа
        return PharmacyStatistics.builder()
                .totalMedications(totalMedications)
                .averagePrice(averagePrice)
                .stockByCategory(stockList)
                .build();
    }

    /**
     * Вспомогательный метод: преобразует gRPC-модель Medication в локальную DTO-модель.
     *
     * @param medication объект из gRPC-ответа
     * @return локальный объект Medication
     */
    private Medication mapToMedication(dev.madela.apteka.proto.medication.Medication medication) {
        return Medication.builder()
                .id(medication.getId())
                .name(medication.getName())
                .price(medication.getPrice())
                .category(medication.getCategory())
                .pharmacyId(medication.getPharmacyId())
                .build();
    }
}
