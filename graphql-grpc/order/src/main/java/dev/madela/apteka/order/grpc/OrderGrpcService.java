package dev.madela.apteka.order.grpc;

import dev.madela.apteka.order.entity.OrderItem;
import dev.madela.apteka.order.service.OrderDomainService;
import dev.madela.apteka.proto.order.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * gRPC-сервис обработки заказов.
 * <p>
 * Реализует интерфейс {@link OrderServiceGrpc.OrderServiceImplBase}, сгенерированный из `order.proto`.
 * Отвечает за создание, отмену и получение заказов, а также проверку лимита.
 * <p>
 * Автор: MaDeLa
 */
@GrpcService
@RequiredArgsConstructor
public class OrderGrpcService extends OrderServiceGrpc.OrderServiceImplBase {

    private final OrderDomainService orderDomainService;

    /**
     * Обрабатывает создание заказа.
     * <p>
     * Получает данные заказа от клиента, сохраняет заказ через доменный сервис,
     * возвращает объект {@code OrderResponse} с полной информацией.
     */
    @Override
    public void createOrder(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        // Преобразуем список из Protobuf в доменные объекты
        List<OrderItem> items = request.getItemsList().stream()
                .map(i -> OrderItem.builder()
                        .medicationId(i.getMedicationId())
                        .quantity(i.getQuantity())
                        .build())
                .toList();

        // Создаём заказ через бизнес-логику
        var savedOrder = orderDomainService.createOrder(request.getUserId(), items);

        // Формируем и отправляем ответ
        OrderResponse response = OrderResponse.newBuilder()
                .setOrder(toProto(savedOrder))
                .build();

        responseObserver.onNext(response);     // отправка результата клиенту
        responseObserver.onCompleted();        // закрытие потока
    }

    /**
     * Обрабатывает отмену заказа по ID.
     */
    @Override
    public void cancelOrder(CancelOrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        var cancelledOrder = orderDomainService.cancelOrder(UUID.fromString(request.getOrderId()));

        OrderResponse response = OrderResponse.newBuilder()
                .setOrder(toProto(cancelledOrder))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * Получение всех заказов пользователя по userId.
     * Также возвращается общее количество.
     */
    @Override
    public void getUserOrders(UserOrdersRequest request, StreamObserver<UserOrdersResponse> responseObserver) {
        var orders = orderDomainService.getOrdersByUser(request.getUserId());

        UserOrdersResponse.Builder builder = UserOrdersResponse.newBuilder();
        builder.addAllOrders(orders.stream().map(this::toProto).toList());
        builder.setTotalCount(orders.size());

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    /**
     * Проверка, разрешено ли пользователю заказать определённое количество товара.
     * Используется для бизнес-валидации (например, ограничений на пользователя).
     */
    @Override
    public void checkOrderLimit(CheckOrderLimitRequest request, StreamObserver<CheckOrderLimitResponse> responseObserver) {
        String userId = request.getUserId();
        int requestedQuantity = request.getRequestedQuantity();

        boolean allowed = orderDomainService.checkOrderLimit(userId, requestedQuantity);

        CheckOrderLimitResponse response = CheckOrderLimitResponse.newBuilder()
                .setAllowed(allowed)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * Преобразует сущность {@link dev.madela.apteka.order.entity.Order} в Protobuf-модель {@link Order}.
     * <p>
     * Используется для формирования ответов клиенту.
     */
    private Order toProto(dev.madela.apteka.order.entity.Order order) {
        return Order.newBuilder()
                .setId(order.getId().toString())
                .setUserId(order.getUserId())
                .setStatus(order.getStatus())
                .setCreatedAt(order.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .addAllItems(
                        order.getItems().stream()
                                .map(item -> dev.madela.apteka.proto.order.OrderItem.newBuilder()
                                        .setMedicationId(item.getMedicationId())
                                        .setQuantity(item.getQuantity())
                                        .build())
                                .toList()
                )
                .build();
    }
}
