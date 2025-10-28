package dev.madela.apteka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyStock {
    private String pharmacyId;
    private String medicationId;
    private int quantity;
    private String expiryDate;
}