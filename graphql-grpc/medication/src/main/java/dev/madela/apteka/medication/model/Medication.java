package dev.madela.apteka.medication.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Medication {
    private UUID id;
    private String name;
    private double price;
    private String category;
    private boolean isPrescriptionOnly;
    private List<ActiveSubstance> activeSubstances;
    private LocalDate expiryDate;
}
