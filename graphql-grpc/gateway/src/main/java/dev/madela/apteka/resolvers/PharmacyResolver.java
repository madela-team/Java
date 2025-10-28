package dev.madela.apteka.resolvers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import dev.madela.apteka.model.MedicationFilter;
import dev.madela.apteka.model.PharmacyMedicationGroup;
import dev.madela.apteka.model.PharmacyStatistics;
import dev.madela.apteka.service.PharmacyService;

import java.util.List;

/**
 * GraphQL-резолвер для работы с аптеками:
 * - Группировка медикаментов по аптекам
 * - Получение статистики по аптеке
 *
 * Автор: MaDeLa
 */
@DgsComponent
public class PharmacyResolver {

    private final PharmacyService pharmacyService;

    public PharmacyResolver(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    /**
     * Обработка запроса:
     * medicationsByPharmacy(filter: MedicationFilter, groupBy: String): [PharmacyMedicationGroup!]!
     *
     * Возвращает сгруппированные медикаменты по аптекам (например, для витрин).
     */
    @DgsQuery
    public List<PharmacyMedicationGroup> medicationsByPharmacy(
            @InputArgument MedicationFilter filter,
            @InputArgument String groupBy
    ) {
        return pharmacyService.getMedicationsGroupedByPharmacy(filter, groupBy);
    }

    /**
     * Обработка запроса:
     * pharmacyStatistics(id: ID!): PharmacyStatistics!
     *
     * Возвращает агрегированную статистику по аптеке.
     */
    @DgsQuery
    public PharmacyStatistics pharmacyStatistics(@InputArgument String id) {
        return pharmacyService.getPharmacyStatistics(id);
    }
}


/***
 * Краткое объяснение используемых DGS-аннотаций:
 * Аннотация	|	Назначение
 * @DgsComponent	|	Делает класс GraphQL-компонентом. Аналог @Component, но для DGS.
 * @DgsQuery	|	Помечает метод как обработчик GraphQL Query (из type Query {})	|
 * @DgsMutation	|	Помечает метод как обработчик GraphQL Mutation (type Mutation {})
 * @InputArgument	|	Извлекает аргумент из запроса GraphQL, аналог @RequestParam в REST
 * @DgsData	|	Обрабатывает отдельное поле объекта, например, виртуальное или внешнее
 * DgsDataFetchingEnvironment	|	Контекст запроса, через который можно получить source, DataLoader, headers и пр.
 */