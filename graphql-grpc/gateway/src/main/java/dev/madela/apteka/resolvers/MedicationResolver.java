package dev.madela.apteka.resolvers;

import com.netflix.graphql.dgs.*;
import dev.madela.apteka.model.*;
import dev.madela.apteka.service.MedicationService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * GraphQL-резолвер для работы с медикаментами.
 *
 * Поддерживает:
 * - Query: medications
 * - Mutations: deleteMedication, deleteExpiredMedications, updateMedicationPrice
 * - Field: Order.medications — агрегация препаратов по их ID
 *
 * Автор: MaDeLa
 */
@DgsComponent
@RequiredArgsConstructor
public class MedicationResolver {

    private final MedicationService medicationService;

    /**
     * Запрос на получение списка медикаментов с фильтрацией и пагинацией.
     *
     * GraphQL-сигнатура:
     * medications(filter: MedicationFilter, pagination: Pagination): MedicationPage!
     */
    @DgsQuery
    public MedicationPage medications(
            @InputArgument MedicationFilter filter,
            @InputArgument Pagination pagination
    ) {
        return medicationService.getMedications(filter, pagination);
    }

    /**
     * Мутация: удалить медикамент по ID.
     *
     * GraphQL-сигнатура:
     * deleteMedication(id: ID!): DeletedMedicationResult!
     */
    @DgsMutation
    public DeletedMedicationResult deleteMedication(@InputArgument String id) {
        return medicationService.deleteMedication(id);
    }

    /**
     * Мутация: удалить все просроченные медикаменты (по дате).
     *
     * GraphQL-сигнатура:
     * deleteExpiredMedications(expiryDate: LocalDate!): DeletedMedicationExpireResult!
     *
     * @param expiryDate дата, до которой препараты считаются просроченными
     */
    @DgsMutation
    public DeletedMedicationExpireResult deleteExpiredMedications(@InputArgument LocalDate expiryDate) {
        return medicationService.deleteMedication(expiryDate);
    }

    /**
     * Мутация: обновить цену конкретного медикамента.
     *
     * GraphQL-сигнатура:
     * updateMedicationPrice(id: ID!, newPrice: Float!): UpdateMedicationPriceResult!
     */
    @DgsMutation
    public UpdateMedicationPriceResult updateMedicationPrice(
            @InputArgument String id,
            @InputArgument double newPrice
    ) {
        return medicationService.updateMedicationPrice(id, newPrice);
    }

    /**
     * Расширенное поле:
     * Order.medications: [Medication!]!
     *
     * Возвращает список медикаментов, входящих в заказ.
     * Используется при агрегации заказов (например, когда GraphQL-запрос идёт по пользователям и заказам).
     *
     * Реализация: достаёт medicationId из Order.items и вызывает medicationService.getMedicationsByIds(...)
     *
     * @param dfe контекст выполнения запроса
     * @return список медикаментов, соответствующих ID
     */
    @DgsData(parentType = "Order", field = "medications")
    public List<Medication> getMedications(DgsDataFetchingEnvironment dfe) {
        Order order = dfe.getSource();
        List<String> medicationIds = order.getItems().stream()
                .map(OrderItem::getMedicationId)
                .distinct()
                .toList();

        return medicationService.getMedicationsByIds(medicationIds);
    }
}

/***
 * Краткое объяснение используемых DGS-аннотаций:
 * Аннотация	|	Назначение
 * @DgsComponent	|	Делает класс GraphQL-компонентом. Аналог @Component, но для DGS.
 * @DgsQuery	|	Помечает метод как обработчик GraphQL Query (из type Query {})	|
 * @DgsMutation	|	Помечает метод как обработчик GraphQL Mutation (type Mutation {})
 * @InputArgument Извлекает аргумент из запроса GraphQL, аналог @RequestParam в REST
 * @DgsData	|	Обрабатывает отдельное поле объекта, например, виртуальное или внешнее
 * DgsDataFetchingEnvironment	|	Контекст запроса, через который можно получить source, DataLoader, headers и пр.
 */
