package dev.madela.apteka.inventory.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Модель данных, представляющая остатки медикаментов в аптеке.
 * <p>
 * Хранится в MongoDB в коллекции {@code pharmacy_stock}.
 */
@Document("pharmacy_stock")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyStock {

    /**
     * Уникальный идентификатор записи в MongoDB.
     * <p>
     * Аннотация {@link org.springframework.data.annotation.Id} указывает Spring Data,
     * что это поле является ключом (_id) документа.
     * <p>
     * Отличие от javax.persistence.Id в том, что здесь речь о NoSQL.
     */
    @Id
    private String id;

    /**
     * Идентификатор аптеки, к которой относится запись.
     */
    private String pharmacyId;

    /**
     * Идентификатор медикамента, для которого хранится остаток.
     */
    private String medicationId;

    /**
     * Количество доступного препарата.
     */
    private int quantity;

    /**
     * Дата истечения срока годности в формате {@code yyyy-MM-dd}.
     */
    private String expiryDate;
}
