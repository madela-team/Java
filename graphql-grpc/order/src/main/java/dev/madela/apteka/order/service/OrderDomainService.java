package dev.madela.apteka.order.service;

import dev.madela.apteka.order.entity.Order;
import dev.madela.apteka.order.entity.OrderItem;
import dev.madela.apteka.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Доменный сервис заказов.
 * <p>
 * Отвечает за бизнес-логику создания, отмены и проверки лимитов по заказам.
 * Взаимодействует с {@link OrderRepository}, но не зависит от gRPC/REST-слоя.
 * <p>
 * Все изменения выполняются в рамках транзакций.
 * <p>
 * Автор: MaDeLa
 */
@Service
@RequiredArgsConstructor
public class OrderDomainService {

    private final OrderRepository orderRepository;

    /**
     * Создаёт новый заказ на основе userId и списка позиций.
     *
     * @param userId ID пользователя
     * @param items  список позиций (без ID, без связей)
     * @return сохранённый заказ со сгенерированными ID
     */
    @Transactional
    public Order createOrder(String userId, List<OrderItem> items) {
        Order order = Order.builder()
                .id(UUID.randomUUID())           // Генерация ID заказа
                .userId(userId)
                .status("CREATED")              // Начальный статус
                .createdAt(LocalDateTime.now()) // Временная метка создания
                .build();

        for (OrderItem item : items) {
            item.setId(UUID.randomUUID()); // Генерация ID позиции
            item.setOrder(order);          // Устанавливаем связь с заказом (нужно для JPA)
        }

        order.setItems(items);

        return orderRepository.save(order); // Сохраняется заказ и каскадно — все позиции
    }

    /**
     * Отменяет существующий заказ.
     *
     * @param orderId UUID заказа
     * @return обновлённый заказ со статусом "CANCELLED"
     * @throws IllegalArgumentException если заказ не найден
     */
    @Transactional
    public Order cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        order.setStatus("CANCELLED");
        return orderRepository.save(order);
    }

    /**
     * Возвращает список заказов пользователя, отсортированных по дате создания (по убыванию).
     *
     * @param userId ID пользователя
     */
    @Transactional(readOnly = true)
    public List<Order> getOrdersByUser(String userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Проверяет, может ли пользователь сделать заказ на указанное количество товара.
     *
     * @param userId            ID пользователя
     * @param requestedQuantity количество, которое пользователь хочет заказать
     * @return true, если не превышен лимит; false — если превышен
     */
    @Transactional(readOnly = true)
    public boolean checkOrderLimit(String userId, int requestedQuantity) {
        int maxAllowedItems = 100;

        // Получаем суммарное количество заказанных товаров
        Integer totalOrderedQuantity = orderRepository.sumQuantityByUserId(userId);
        if (totalOrderedQuantity == null) {
            totalOrderedQuantity = 0; // если заказов ещё не было
        }

        return (totalOrderedQuantity + requestedQuantity) <= maxAllowedItems;
    }
}
