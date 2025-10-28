package dev.madela.apteka.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * JPA-сущность "OrderItem" — позиция в заказе.
 * <p>
 * Представляет связь между заказом и конкретным медикаментом,
 * включая количество.
 * <p>
 * Отображается на таблицу {@code order_items} в базе данных.
 * <p>
 * Автор: MaDeLa
 */
@Entity
@Table(name = "order_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    /**
     * Уникальный идентификатор позиции (UUID).
     * Генерируется на уровне приложения.
     */
    @Id
    private UUID id;

    /**
     * Ссылка на родительский заказ.
     * <p>
     * - `@ManyToOne` — многие позиции могут принадлежать одному заказу.
     * - `fetch = FetchType.LAZY` — заказ загружается только при явном доступе.
     * - `@JoinColumn(name = "order_id")` — внешний ключ в таблице `order_items`.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    /**
     * Идентификатор медикамента, добавленного в заказ.
     * <p>
     * Хранится как строка (может быть UUID в текстовом виде).
     * Это упрощает декомпозицию между сервисами (не тянем foreign key).
     */
    @Column(name = "medication_id", nullable = false)
    private String medicationId;

    /**
     * Количество единиц медикамента в заказе.
     * Значение всегда положительное.
     */
    @Column(nullable = false)
    private int quantity;
}
