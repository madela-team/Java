package dev.madela.apteka.resolvers;

import com.netflix.graphql.dgs.*;
import dev.madela.apteka.model.Order;
import dev.madela.apteka.model.OrderInput;
import dev.madela.apteka.service.OrderService;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * GraphQL-резолвер для работы с заказами.
 *
 * Поддерживает:
 * - Query: orders(userId)
 * - Mutation: createOrder(input)
 * - Расширенное поле: Order.total
 *
 * Автор: MaDeLa
 */
@DgsComponent
@RequiredArgsConstructor
public class OrderResolver {

    private final OrderService orderService;

    /**
     * Обрабатывает GraphQL-запрос:
     * orders(userId: ID!): [Order!]!
     *
     * Возвращает список заказов по конкретному пользователю.
     */
    @DgsQuery
    public List<Order> orders(@InputArgument String userId) {
        return orderService.getOrdersByUser(userId);
    }

    /**
     * Обрабатывает GraphQL-мутацию:
     * createOrder(input: OrderInput!): Order!
     *
     * Создаёт новый заказ по входным данным.
     */
    @DgsMutation
    public Order createOrder(@InputArgument OrderInput input) {
        return orderService.createOrder(input);
    }

    /**
     * Расширенное поле Order.total — вычисляется на лету.
     *
     * Возвращает общую сумму заказа:
     * сумма по всем позициям = price * quantity.
     *
     * GraphQL-сигнатура:
     * type Order {
     *     total: Float!
     * }
     */
    @DgsData(parentType = "Order", field = "total")
    public Double getTotal(DgsDataFetchingEnvironment dfe) {
        Order order = dfe.getSource();
        return order.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
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