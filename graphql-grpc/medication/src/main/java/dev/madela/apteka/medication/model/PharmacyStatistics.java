package dev.madela.apteka.medication.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PharmacyStatistics {
    private int totalMedications;
    private double averagePrice;
    private List<CategoryStock> stockByCategory;
}
