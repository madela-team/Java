package dev.madela.apteka.resolvers;

import dev.madela.apteka.model.PharmacyStock;
import com.netflix.graphql.dgs.*;
import dev.madela.apteka.proto.medication.Medication;
import lombok.RequiredArgsConstructor;
import dev.madela.apteka.service.InventoryService;
import org.dataloader.DataLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * GraphQL-резолвер для получения остатков медикаментов по аптекам.
 *
 * Работает поверх Netflix DGS и взаимодействует с InventoryService (внутренним обёрткой над gRPC).
 *
 * Поддерживает:
 * - Query: stockByMedication(medicationId)
 * - Расширенное поле: Medication.pharmacyStock (с использованием DataLoader)
 *
 * Автор: MaDeLa
 */
@DgsComponent
@RequiredArgsConstructor
public class InventoryResolver {

    // Сервис, через который идёт вызов gRPC-InventoryService
    private final InventoryService inventoryService;

    /**
     * Обработка GraphQL-запроса:
     * stockByMedication(medicationId: ID!): [PharmacyStock!]!
     *
     * Аннотация @DgsQuery связывает метод с Query-операцией из схемы inventory.graphqls
     *
     * @param medicationId ID препарата
     * @return список остатков этого препарата по аптекам
     */
    @DgsQuery
    public List<PharmacyStock> stockByMedication(@InputArgument String medicationId) {
        return inventoryService.getStock(medicationId);
    }

    /**
     * Расширенное поле pharmacyStock внутри типа Medication:
     *
     * type Medication {
     *     ...
     *     pharmacyStock: [PharmacyStock!]! @external
     * }
     *
     * Использует DataLoader для оптимизации — чтобы не делать N gRPC-запросов на каждый препарат в списке.
     * DGS группирует вызовы автоматически и вызывает один batched-запрос в InventoryDataLoader.
     *
     * @param dfe контекст выполнения запроса (DgsDataFetchingEnvironment)
     * @return CompletableFuture с результатом — необходимо для асинхронной загрузки в DataLoader
     */
    @DgsData(parentType = "Medication", field = "pharmacyStock")
    public CompletableFuture<List<PharmacyStock>> getPharmacyStock(DgsDataFetchingEnvironment dfe) {
        Medication medication = dfe.getSource(); // Получаем текущий объект типа Medication
        DataLoader<String, List<PharmacyStock>> loader = dfe.getDataLoader("InventoryDataLoader");
        return loader.load(medication.getId());
    }
}


/***
 * Что происходит при GraphQL-запросе:
 * {
 *   medications {
 *     id
 *     name
 *     pharmacyStock {
 *       pharmacyId
 *       quantity
 *     }
 *   }
 * }
 * pharmacyStock для всех медикаментов будет собран через один DataLoader-запрос вместо N отдельных gRPC вызовов.
 *
 * Это даёт прирост производительности и уменьшает сетевую нагрузку.
 */