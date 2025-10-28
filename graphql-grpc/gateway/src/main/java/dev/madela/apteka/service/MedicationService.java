package dev.madela.apteka.service;

import dev.madela.apteka.model.ActiveSubstance;
import dev.madela.apteka.model.Medication;
import dev.madela.apteka.model.*;
import dev.madela.apteka.proto.inventory.DeleteStockByMedicationRequest;
import dev.madela.apteka.proto.inventory.DeleteStockByMedicationResponse;
import dev.madela.apteka.proto.inventory.InventoryServiceGrpc;
import dev.madela.apteka.proto.medication.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Сервис для взаимодействия с medication-сервисом через gRPC.
 * Позволяет получать, обновлять и удалять медикаменты.
 * Взаимодействует также с inventory-сервисом для синхронного удаления остатков.
 *
 * Автор: MaDeLa
 */
@Service
@RequiredArgsConstructor
public class MedicationService {

    /**
     * gRPC-клиент для обращения к medication-сервису.
     */
    private final MedicationServiceGrpc.MedicationServiceBlockingStub grpcClient;

    /**
     * gRPC-клиент для обращения к inventory-сервису.
     * Используется при удалении медикаментов, чтобы также почистить остатки.
     */
    private final InventoryServiceGrpc.InventoryServiceBlockingStub inventoryGrpcClient;

    /**
     * Получает страницу медикаментов с возможностью фильтрации и пагинации.
     *
     * @param filter     фильтры по категориям, цене и веществам
     * @param pagination параметры пагинации (номер страницы и размер)
     * @return объект с общей выборкой и количеством всех записей
     */
    public MedicationPage getMedications(MedicationFilter filter, Pagination pagination) {
        int page = pagination != null ? pagination.getPage() : 0;
        int size = pagination != null ? pagination.getSize() : 20;

        var protoPagination = dev.madela.apteka.proto.common.Pagination.newBuilder()
                .setPage(page)
                .setSize(size)
                .build();

        var requestBuilder = MedicationRequest.newBuilder()
                .setCategory(filter.getCategory() == null ? "" : filter.getCategory())
                .setPagination(protoPagination);

        if (filter.getPriceRange() != null) {
            var priceRange = dev.madela.apteka.proto.medication.PriceRange.newBuilder()
                    .setMin(filter.getPriceRange().getMin())
                    .setMax(filter.getPriceRange().getMax())
                    .build();
            requestBuilder.setPriceRange(priceRange);
        }

        var request = requestBuilder.build();

        MedicationsResponse response = grpcClient.getMedications(request);
        List<Medication> items = response.getMedicationsList().stream()
                .map(this::toDto)
                .toList();

        return MedicationPage.builder()
                .items(items)
                .totalCount(response.getTotalCount())
                .build();
    }

    /**
     * Удаляет медикамент по ID. Также удаляет все его остатки через inventory-сервис.
     *
     * @param id идентификатор медикамента
     * @return результат удаления (id, название и успешность)
     */
    public DeletedMedicationResult deleteMedication(String id) {
        MedicationResponse medicationResponse = grpcClient.getMedicationById(
                MedicationIdRequest.newBuilder().setId(id).build()
        );
        String name = medicationResponse.getMedication().getName();

        grpcClient.deleteMedication(DeleteMedicationRequest.newBuilder()
                .setId(medicationResponse.getMedication().getId())
                .build());

        DeleteStockByMedicationResponse deleted = inventoryGrpcClient.deleteStockByMedication(
                DeleteStockByMedicationRequest.newBuilder().setMedicationId(id).build()
        );

        return DeletedMedicationResult.builder()
                .id(id)
                .name(name)
                .success(deleted.getSuccess())
                .build();
    }

    /**
     * Удаляет медикаменты, срок годности которых истёк.
     *
     * @param expiryDate дата, до которой медикаменты считаются просроченными
     * @return результат удаления: количество и список ID удалённых медикаментов
     */
    public DeletedMedicationExpireResult deleteMedication(LocalDate expiryDate) {
        String expiryDateStr = expiryDate.toString();

        DeleteExpiredMedicationsResponse response = grpcClient.deleteExpiredMedications(
                DeleteExpiredMedicationsRequest.newBuilder()
                        .setExpiryDate(expiryDateStr)
                        .build()
        );

        return DeletedMedicationExpireResult.builder()
                .count(response.getCount())
                .deletedIds(response.getDeletedIdsList())
                .build();
    }

    /**
     * Получает список медикаментов по их ID.
     *
     * @param ids список ID
     * @return список объектов Medication
     */
    public List<Medication> getMedicationsByIds(List<String> ids) {
        var request = dev.madela.apteka.proto.medication.MedicationIdsRequest.newBuilder()
                .addAllIds(ids)
                .build();

        MedicationsResponse response = grpcClient.getMedicationsByIds(request);

        return response.getMedicationsList().stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Обновляет цену медикамента. Предварительно получает старую цену.
     *
     * @param id       ID медикамента
     * @param newPrice новая цена
     * @return объект с деталями обновления (id, имя, старая и новая цена, дата обновления)
     */
    public UpdateMedicationPriceResult updateMedicationPrice(String id, double newPrice) {
        MedicationResponse medicationResponse = grpcClient.getMedicationById(
                MedicationIdRequest.newBuilder().setId(id).build()
        );

        double oldPrice = medicationResponse.getMedication().getPrice();

        UpdateMedicationPriceResponse response = grpcClient.updateMedicationPrice(
                UpdateMedicationPriceRequest.newBuilder()
                        .setId(id)
                        .setOldPrice(oldPrice)
                        .setNewPrice(newPrice)
                        .build()
        );

        return UpdateMedicationPriceResult.builder()
                .id(response.getId())
                .name(response.getName())
                .oldPrice(response.getOldPrice())
                .newPrice(response.getNewPrice())
                .updatedAt(response.getUpdatedAt())
                .build();
    }

    /**
     * Преобразует protobuf-объект медикамента в локальную DTO-модель.
     *
     * @param medicationProto объект из gRPC-ответа
     * @return локальный DTO-медикамент
     */
    private Medication toDto(dev.madela.apteka.proto.medication.Medication medicationProto) {
        return Medication.builder()
                .id(medicationProto.getId())
                .name(medicationProto.getName())
                .price(medicationProto.getPrice())
                .category(medicationProto.getCategory())
                .substances(CollectionUtils.isEmpty(medicationProto.getActiveSubstancesList())
                        ? Collections.emptyList()
                        : medicationProto.getActiveSubstancesList().stream()
                        .map(sub -> new ActiveSubstance(sub.getName(), sub.getConcentration()))
                        .toList())
                .build();
    }
}
