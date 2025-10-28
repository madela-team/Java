package dev.madela.apteka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medication {
    private String id;
    private String name;
    private double price;
    private String category;
    private boolean isPrescriptionOnly;
    @Builder.Default
    private List<ActiveSubstance> substances = Collections.emptyList();
    @Builder.Default
    private List<PharmacyStock> pharmacyStocks = Collections.emptyList();
    private String pharmacyId;
}