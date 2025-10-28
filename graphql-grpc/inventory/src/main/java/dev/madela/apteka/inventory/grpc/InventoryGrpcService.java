package dev.madela.apteka.inventory.grpc;

import dev.madela.apteka.inventory.service.InventoryService;
import dev.madela.apteka.proto.inventory.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * gRPC-сервис, реализующий бизнес-логику работы с остатками медикаментов в аптеке.
 * <p>
 * Оборачивает вызовы сервиса {@link InventoryService} в gRPC-интерфейс.
 * Реализует методы, определённые в .proto-файле {@code inventory-service.proto}.
 * <p>
 * Используется библиотека {@code grpc-server-spring-boot-starter} для регистрации сервиса.
 * <p>
 * Автор: MaDeLa
 */
@GrpcService
@RequiredArgsConstructor
public class InventoryGrpcService extends InventoryServiceGrpc.InventoryServiceImplBase {

    private final InventoryService inventoryService;

    /**
     * Получение остатков по конкретному медикаменту.
     *
     * @param request           содержит {@code medication_id} (и опционально {@code pharmacy_id})
     * @param responseObserver  используется для возврата ответа клиенту
     */
    @Override
    public void getStock(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        var stocks = inventoryService.getStock(request.getMedicationId());
        var response = StockResponse.newBuilder()
                .addAllStocks(stocks.stream().map(this::toProto).toList())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * Обновление остатков медикаментов по аптеке.
     *
     * @param request           содержит идентификатор аптеки и список обновлений {@code medication_id + delta}
     * @param responseObserver  возвращает обновлённый список остатков
     */
    @Override
    public void updateStock(UpdateStockRequest request, StreamObserver<StockResponse> responseObserver) {
        var updates = request.getUpdatesList().stream()
                .map(update -> dev.madela.apteka.inventory.model.PharmacyStock.builder()
                        .pharmacyId(request.getPharmacyId())
                        .medicationId(update.getMedicationId())
                        .quantity(update.getDelta()) // может быть отрицательным
                        .build())
                .toList();

        var updated = inventoryService.updateStock(request.getPharmacyId(), updates);
        var response = StockResponse.newBuilder()
                .addAllStocks(updated.stream().map(this::toProto).toList())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * Получение просроченных медикаментов по дате истечения срока годности.
     *
     * @param request           содержит дату истечения срока в формате YYYY-MM-DD
     * @param responseObserver  возвращает список просроченных остатков
     */
    @Override
    public void getExpiredMedications(ExpiredRequest request, StreamObserver<StockResponse> responseObserver) {
        var expired = inventoryService.getExpiredMedications(request.getExpiryDate());
        var response = StockResponse.newBuilder()
                .addAllStocks(expired.stream().map(this::toProto).toList())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * Удаление всех записей об остатках по определённому медикаменту.
     *
     * @param request           содержит {@code medication_id}
     * @param responseObserver  возвращает флаг успеха удаления
     */
    @Override
    public void deleteStockByMedication(DeleteStockByMedicationRequest request, StreamObserver<DeleteStockByMedicationResponse> responseObserver) {
        boolean success = inventoryService.deleteByMedicationId(request.getMedicationId());
        var response = DeleteStockByMedicationResponse.newBuilder().setSuccess(success).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * Вспомогательный метод преобразования модели из доменного слоя в gRPC-формат.
     *
     * @param stock объект {@link dev.madela.apteka.inventory.model.PharmacyStock}
     * @return      объект {@link dev.madela.apteka.proto.inventory.PharmacyStock}
     */
    private dev.madela.apteka.proto.inventory.PharmacyStock toProto(dev.madela.apteka.inventory.model.PharmacyStock stock) {
        return dev.madela.apteka.proto.inventory.PharmacyStock.newBuilder()
                .setPharmacyId(stock.getPharmacyId())
                .setMedicationId(stock.getMedicationId())
                .setQuantity(stock.getQuantity())
                .setExpiryDate(stock.getExpiryDate())
                .build();
    }
}
