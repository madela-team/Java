package dev.madela.apteka.service;

import dev.madela.apteka.model.OrderInput;
import dev.madela.apteka.model.OrderItemInput;
import dev.madela.apteka.model.PharmacyStock;
import dev.madela.apteka.proto.order.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Сервис для работы с заказами. Взаимодействует с order-сервисом по gRPC,
 * а также использует inventory и user-сервисы для валидации данных перед созданием заказа.
 *
 * Автор: MaDeLa
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderServiceGrpc.OrderServiceBlockingStub grpcClient;
    private final InventoryService inventoryService;
    private final UserService userService;

    /**
     * Получает список заказов по ID пользователя.
     *
     * @param userId ID пользователя
     * @return список заказов, оформленных этим пользователем
     */
    public List<dev.madela.apteka.model.Order> getOrdersByUser(String userId) {
        var request = UserOrdersRequest.newBuilder().setUserId(userId).build();
        var response = grpcClient.getUserOrders(request);
        return response.getOrdersList().stream()
                .map(this::toDto)
                .collect(toList());
    }

    /**
     * Создаёт заказ. Предварительно проходит три уровня проверки:
     * 1. Проверка существования пользователя
     * 2. Проверка наличия медикаментов на складе
     * 3. Проверка ограничения по количеству заказов
     *
     * @param input входной DTO для создания заказа
     * @return созданный заказ
     * @throws IllegalStateException если нарушено одно из условий
     */
    public dev.madela.apteka.model.Order createOrder(OrderInput input) {
        // 0. Проверка существования пользователя
        var user = userService.getUserById(input.getUserId());
        if (user == null) {
            throw new IllegalStateException("Пользователь не найден: " + input.getUserId());
        }

        // 1. Проверка остатков по каждому медикаменту
        for (OrderItemInput item : input.getItems()) {
            var stock = inventoryService.getStock(item.getMedicationId());
            var totalAvailable = stock.stream()
                    .mapToInt(PharmacyStock::getQuantity)
                    .sum();
            if (totalAvailable < item.getQuantity()) {
                throw new IllegalStateException("Недостаточно остатков для медикамента: " + item.getMedicationId());
            }
        }

        // 2. Проверка лимита заказов (например, чтобы нельзя было оформить больше 10 за раз)
        boolean allowed = checkOrderLimit(input);
        if (!allowed) {
            throw new IllegalStateException("Превышен лимит заказов пользователя");
        }

        // 3. Формирование gRPC-запроса на создание заказа
        var requestBuilder = OrderRequest.newBuilder()
                .setUserId(input.getUserId())
                .setDeliveryAddress(input.getDeliveryAddress() != null ? input.getDeliveryAddress() : "");

        for (OrderItemInput item : input.getItems()) {
            requestBuilder.addItems(
                    dev.madela.apteka.proto.order.OrderItem.newBuilder()
                            .setMedicationId(item.getMedicationId())
                            .setQuantity(item.getQuantity())
                            .build()
            );
        }

        var response = grpcClient.createOrder(requestBuilder.build());
        return toDto(response);
    }

    /**
     * Внутренний метод: проверяет, можно ли оформить заказ, не превышен ли лимит.
     * Лимит может быть установлен на уровне бизнес-логики бэкенда.
     *
     * @param input DTO с товарами заказа
     * @return true, если заказ разрешён; false — если превышен лимит
     */
    private boolean checkOrderLimit(OrderInput input) {
        int requestedQuantity = input.getItems().stream()
                .mapToInt(OrderItemInput::getQuantity)
                .sum();
        return grpcClient.checkOrderLimit(
                CheckOrderLimitRequest.newBuilder()
                        .setUserId(input.getUserId())
                        .setRequestedQuantity(requestedQuantity)
                        .build()
        ).getAllowed();
    }

    /**
     * Преобразует ответ gRPC-сервиса (обёртку OrderResponse) в DTO-модель Order.
     *
     * @param response ответ gRPC
     * @return модель заказа
     */
    private dev.madela.apteka.model.Order toDto(OrderResponse response) {
        return toDto(response.getOrder());
    }

    /**
     * Преобразует gRPC-объект Order в локальный DTO.
     *
     * @param proto объект из protobuf
     * @return DTO заказ
     */
    private dev.madela.apteka.model.Order toDto(Order proto) {
        return dev.madela.apteka.model.Order.builder()
                .id(proto.getId())
                .userId(proto.getUserId())
                .status(proto.getStatus())
                .createdAt(LocalDateTime.parse(proto.getCreatedAt()))
                .items(proto.getItemsList().stream()
                        .map(i -> dev.madela.apteka.model.OrderItem.builder()
                                .medicationId(i.getMedicationId())
                                .quantity(i.getQuantity())
                                .build())
                        .toList())
                .build();
    }
}
