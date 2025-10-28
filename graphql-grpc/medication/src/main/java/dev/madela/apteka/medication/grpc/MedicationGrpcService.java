package dev.madela.apteka.medication.grpc;

import dev.madela.apteka.medication.model.Medication;
import dev.madela.apteka.medication.model.ActiveSubstance;
import dev.madela.apteka.medication.service.MedicationService;
import dev.madela.apteka.proto.medication.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * gRPC-сервис для работы с медикаментами.
 * <p>
 * Реализует protobuf-интерфейс {@code MedicationServiceGrpc.MedicationServiceImplBase}.
 * Сервис предоставляет CRUD-операции для медикаментов и аналитическую информацию по аптекам.
 * Используется библиотека <a href="https://github.com/yidongnan/grpc-spring-boot-starter">grpc-spring-boot-starter</a>.
 * <p>
 * Автор: MaDeLa
 */
@GrpcService
@RequiredArgsConstructor
public class MedicationGrpcService extends MedicationServiceGrpc.MedicationServiceImplBase {

    private final MedicationService medicationService;

    /**
     * Получение списка медикаментов по фильтрам (категория, диапазон цен, пагинация).
     */
    @Override
    public void getMedications(MedicationRequest request, StreamObserver<MedicationsResponse> responseObserver) {
        try {
            String category = request.getCategory().isEmpty() ? null : request.getCategory();
            Double minPrice = request.hasPriceRange() ? request.getPriceRange().getMin() : null;
            Double maxPrice = request.hasPriceRange() ? request.getPriceRange().getMax() : null;

            int page = request.hasPagination() ? request.getPagination().getPage() : 0;
            int size = request.hasPagination() ? request.getPagination().getSize() : 20;

            var medications = medicationService.getFilteredMedications(category, minPrice, maxPrice, page, size);
            int total = medicationService.getFilteredCount(category, minPrice, maxPrice);

            var response = MedicationsResponse.newBuilder()
                    .addAllMedications(medications.stream().map(this::toProtoSafe).toList())
                    .setTotalCount(total)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            // gRPC-ошибка с кодом INTERNAL — общая ошибка сервера
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Failed to get medications: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    /**
     * Получение медикамента по его ID.
     */
    @Override
    public void getMedicationById(MedicationIdRequest request, StreamObserver<MedicationResponse> responseObserver) {
        try {
            UUID id = UUID.fromString(request.getId());

            Medication medication = medicationService.getById(id);
            if (medication == null) {
                responseObserver.onError(Status.NOT_FOUND
                        .withDescription("Medication not found with ID: " + request.getId())
                        .asRuntimeException());
                return;
            }

            var response = MedicationResponse.newBuilder()
                    .setMedication(toProtoSafe(medication))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID: " + request.getId())
                    .withCause(e)
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal error while fetching medication: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    /**
     * Получение списка медикаментов по списку идентификаторов.
     */
    @Override
    public void getMedicationsByIds(MedicationIdsRequest request, StreamObserver<MedicationsResponse> responseObserver) {
        List<Medication> medications = medicationService.findByIds(request.getIdsList().stream().toList());
        var response = MedicationsResponse.newBuilder()
                .addAllMedications(medications.stream().map(this::toProtoSafe).toList())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * Обновление цены у медикамента.
     */
    @Override
    public void updateMedicationPrice(UpdateMedicationPriceRequest request,
                                      StreamObserver<UpdateMedicationPriceResponse> responseObserver) {
        try {
            UUID id = UUID.fromString(request.getId());
            double newPrice = request.getNewPrice();

            Medication updated = medicationService.updatePrice(id, newPrice);
            if (updated == null) {
                responseObserver.onError(Status.NOT_FOUND
                        .withDescription("Medication not found to update price: " + request.getId())
                        .asRuntimeException());
                return;
            }

            var response = UpdateMedicationPriceResponse.newBuilder()
                    .setId(updated.getId().toString())
                    .setName(updated.getName())
                    .setOldPrice(request.getOldPrice())
                    .setNewPrice(updated.getPrice())
                    .setUpdatedAt(Instant.now().toString())  // UTC-время изменения
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID: " + request.getId())
                    .withCause(e)
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Failed to update price: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    /**
     * Удаление медикамента по ID.
     */
    @Override
    public void deleteMedication(DeleteMedicationRequest request, StreamObserver<DeleteMedicationResponse> responseObserver) {
        try {
            UUID id = UUID.fromString(request.getId());

            Medication medication = medicationService.findById(request.getId());
            if (medication == null) {
                responseObserver.onError(Status.NOT_FOUND
                        .withDescription("Medication not found for deletion: " + request.getId())
                        .asRuntimeException());
                return;
            }

            medicationService.deleteById(id);

            var response = DeleteMedicationResponse.newBuilder()
                    .setId(medication.getId().toString())
                    .setName(medication.getName())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID: " + request.getId())
                    .withCause(e)
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error deleting medication: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    /**
     * Массовое удаление просроченных медикаментов (по дате истечения срока годности).
     */
    @Override
    public void deleteExpiredMedications(DeleteExpiredMedicationsRequest request, StreamObserver<DeleteExpiredMedicationsResponse> responseObserver) {
        try {
            LocalDate expiryDate = LocalDate.parse(request.getExpiryDate());
            List<UUID> deletedIds = medicationService.deleteExpiredMedications(expiryDate);

            var response = DeleteExpiredMedicationsResponse.newBuilder()
                    .setCount(deletedIds.size())
                    .addAllDeletedIds(deletedIds.stream().map(UUID::toString).toList())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error deleting expired medications: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    /**
     * Получение всех медикаментов, привязанных к определённой аптеке.
     */
    @Override
    public void getMedicationsByPharmacy(PharmacyMedicationsRequest request, StreamObserver<PharmacyMedicationsResponse> responseObserver) {
        try {
            List<Medication> medications = medicationService.findByPharmacyId(UUID.fromString(request.getPharmacyId()));
            var response = PharmacyMedicationsResponse.newBuilder()
                    .addAllMedications(medications.stream().map(this::toProtoSafe).toList())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    /**
     * Получение статистики по аптеке: количество препаратов, средняя цена, разбивка по категориям.
     */
    @Override
    public void getPharmacyStatistics(PharmacyStatisticsRequest request, StreamObserver<PharmacyStatisticsResponse> responseObserver) {
        try {
            var stats = medicationService.getPharmacyStatistics(UUID.fromString(request.getPharmacyId()));

            var response = PharmacyStatisticsResponse.newBuilder()
                    .setTotalMedications(stats.getTotalMedications())
                    .setAveragePrice(stats.getAveragePrice())
                    .addAllStockByCategory(
                            stats.getStockByCategory().stream()
                                    .map(cs -> CategoryStock.newBuilder()
                                            .setCategory(cs.getCategory())
                                            .setCount(cs.getCount())
                                            .build())
                                    .toList()
                    )
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    /**
     * Преобразует доменную модель {@link Medication} в protobuf-сообщение.
     */
    private dev.madela.apteka.proto.medication.Medication toProtoSafe(Medication m) {
        var builder = dev.madela.apteka.proto.medication.Medication.newBuilder()
                .setId(m.getId().toString())
                .setName(m.getName() != null ? m.getName() : "")
                .setPrice(m.getPrice())
                .setCategory(m.getCategory() != null ? m.getCategory() : "");

        if (m.getActiveSubstances() != null) {
            builder.addAllActiveSubstances(m.getActiveSubstances().stream()
                    .map(this::toProto)
                    .collect(Collectors.toList()));
        }

        return builder.build();
    }

    /**
     * Преобразует {@link ActiveSubstance} в protobuf-сообщение.
     */
    private dev.madela.apteka.proto.medication.ActiveSubstance toProto(ActiveSubstance s) {
        return dev.madela.apteka.proto.medication.ActiveSubstance.newBuilder()
                .setName(s.getName() != null ? s.getName() : "")
                .setConcentration(s.getConcentration())
                .build();
    }
}

/**
 * StreamObserver: управляет жизненным циклом ответа.
 *
 * Использование Status.INTERNAL, Status.NOT_FOUND, Status.INVALID_ARGUMENT — ключевой способ сообщать ошибки клиенту.
 *
 * Преобразование доменных объектов в protobuf — важный навык для построения изолированных слоёв.
 * -------------------------------------
 * responseObserver.onNext(response);
 *
 * Метод отправляет данные клиенту.
 * Это основной способ передать результат выполнения RPC-запроса. Если бы это был стриминг, таких вызовов могло бы быть несколько. Здесь — один раз передаём response.
 * --------------------------------------
 * responseObserver.onCompleted();
 *
 * Сообщает клиенту, что передача данных завершена.
 * Без этого вызова gRPC считает, что ответ ещё не окончен. Это обязательный шаг даже при unary-вызовах (один запрос — один ответ).
 * ---------------------------------------
 * Если сравнивать с обычным HTTP, то:
 *
 *     onNext() — как тело ответа (body),
 *
 *     onCompleted() — как закрытие HTTP-соединения с финальной точкой передачи.
 *
 * Без onCompleted() клиент "зависнет" в ожидании завершения.
 */