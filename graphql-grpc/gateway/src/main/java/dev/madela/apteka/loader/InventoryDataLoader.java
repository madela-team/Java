package dev.madela.apteka.loader;

import com.netflix.graphql.dgs.DgsComponent;
import dev.madela.apteka.model.PharmacyStock;
import dev.madela.apteka.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * DataLoader-компонент для загрузки остатков по списку medicationId.
 *
 * Используется в GraphQL как батч-загрузчик (batch loader) для уменьшения количества запросов к gRPC.
 * Вместо того чтобы загружать stock по каждому medicationId отдельно, данные собираются пачкой.
 *
 * Пример использования в DGS: можно подключать к полям через @DgsData с DgsDataFetchingEnvironment.getDataLoader(...)
 *
 * Автор: MaDeLa
 */
@DgsComponent
@RequiredArgsConstructor
public class InventoryDataLoader implements BatchLoader<String, List<PharmacyStock>> {

    private final InventoryService inventoryService;

    /**
     * Метод, вызываемый DGS DataLoader'ом.
     * На вход получает список medicationId, по которым нужно получить остатки.
     * Возвращает List<List<PharmacyStock>>, где каждый вложенный список соответствует medicationId из входного списка.
     *
     * @param medicationIds список ID медикаментов
     * @return асинхронный результат с остатками по каждому ID
     */
    @Override
    public CompletionStage<List<List<PharmacyStock>>> load(List<String> medicationIds) {
        // Асинхронно получаем map<medicationId, List<PharmacyStock>> через gRPC
        return CompletableFuture.supplyAsync(() -> {
            Map<String, List<PharmacyStock>> stockMap = inventoryService.getStockByMedicationIds(medicationIds);

            // Преобразуем map в List<List<...>> строго в том порядке, в котором пришли medicationIds
            return medicationIds.stream()
                    .map(id -> stockMap.getOrDefault(id, List.of()))
                    .collect(Collectors.toList());
        });
    }
}

/***
 * !!! Важно: этот класс не обрабатывает DgsData напрямую — он только реализует логику загрузки для DataLoader.
 * Его нужно регистрировать под уникальным именем, если в проекте явно используется @DgsData(parentType, field) + .getDataLoader("название").
 *
 */