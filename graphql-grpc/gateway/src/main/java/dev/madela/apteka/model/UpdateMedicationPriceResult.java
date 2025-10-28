package dev.madela.apteka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMedicationPriceResult {
    private String id;
    private String name;
    private double oldPrice;
    private double newPrice;
    private String updatedAt;
}