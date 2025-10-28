package dev.madela.apteka.service;

import dev.madela.apteka.model.PharmacyStock;
import dev.madela.apteka.proto.inventory.InventoryServiceGrpc;
import dev.madela.apteka.proto.inventory.MedicationIdsRequest;
import dev.madela.apteka.proto.inventory.StockRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервис для взаимодействия с inventory-сервисом через gRPC.
 * Отвечает за получение информации о наличии лекарств в аптеках.
 * Используется в GraphQL-резолверах и DataLoader'ах.
 *
 * Автор: MaDeLa
 */
@Service
@RequiredArgsConstructor
public class InventoryService {

    /**
     * gRPC-клиент (blocking stub), сгенерированный на основе inventory.proto.
     * Используется для вызова методов inventory-сервиса.
     */
    private final InventoryServiceGrpc.InventoryServiceBlockingStub grpcClient;

    /**
     * Получает список остатков по одному лекарству.
     *
     * @param medicationId ID медикамента
     * @return список объектов PharmacyStock, отражающих наличие данного медикамента по аптекам
     */
    public List<PharmacyStock> getStock(String medicationId) {
        var request = StockRequest.newBuilder()
                .setMedicationId(medicationId)
                .build();

        var response = grpcClient.getStock(request);

        // Преобразуем protobuf-ответ в локальную DTO-модель
        return response.getStocksList().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получает список остатков по нескольким медикаментам.
     * Метод удобен для массовых запросов из DataLoader'ов.
     *
     * @param medicationIds список ID медикаментов
     * @return Map, где ключ — ID медикамента, а значение — список остатков по аптекам
     */
    public Map<String, List<PharmacyStock>> getStockByMedicationIds(List<String> medicationIds) {
        var request = MedicationIdsRequest.newBuilder()
                .addAllMedicationIds(medicationIds)
                .build();

        var response = grpcClient.getStocksByMedicationIds(request);

        // Группируем ответ по medicationId
        return response.getStocksList().stream()
                .map(this::toDto)
                .collect(Collectors.groupingBy(PharmacyStock::getMedicationId));
    }

    /**
     * Преобразует protobuf-модель PharmacyStock в локальную DTO-модель.
     * Используется внутри публичных методов.
     *
     * @param proto объект из gRPC-ответа
     * @return PharmacyStock DTO, пригодный для использования в GraphQL
     */
    private PharmacyStock toDto(dev.madela.apteka.proto.inventory.PharmacyStock proto) {
        return PharmacyStock.builder()
                .pharmacyId(proto.getPharmacyId())
                .medicationId(proto.getMedicationId())
                .quantity(proto.getQuantity())
                .expiryDate(proto.getExpiryDate())
                .build();
    }
}
