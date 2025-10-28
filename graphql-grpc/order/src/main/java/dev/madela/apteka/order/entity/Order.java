package dev.madela.apteka.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * JPA-сущность "Order" — заказ пользователя.
 * <p>
 * Отображается на таблицу {@code orders} в базе данных.
 * Содержит основную информацию о заказе, включая список позиций (order items).
 * Используется в микросервисе {@code order-service}.
 * <p>
 * Автор: MaDeLa
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    /**
     * Уникальный идентификатор заказа (UUID).
     * Устанавливается на стороне приложения.
     */
    @Id
    private UUID id;

    /**
     * Идентификатор пользователя, сделавшего заказ.
     * Может быть строкой (например, external ID из другого сервиса).
     */
    @Column(name = "user_id", nullable = false)
    private String userId;

    /**
     * Статус заказа, например: "NEW", "PAID", "CANCELLED", "DELIVERED".
     */
    @Column(nullable = false)
    private String status;

    /**
     * Дата и время создания заказа.
     * Используется как базовая временная метка (например, для сортировки).
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Список позиций заказа (товаров), связанных с данным заказом.
     * <p>
     * - `mappedBy = "order"` — связь двусторонняя, обратная сторона — поле `order` в {@link OrderItem}
     * - `cascade = CascadeType.ALL` — при сохранении/удалении заказа, позиции обрабатываются автоматически
     * - `orphanRemoval = true` — если позиция удаляется из списка, она также удаляется из базы
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;
}
