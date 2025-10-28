package dev.madela.apteka.model;

import dev.madela.apteka.proto.medication.ActiveSubstance;
import lombok.AllArgsConstructor;
import lombok.Data;
import dev.madela.apteka.model.PriceRange;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicationFilter {
    private String category;
    private List<String> categories;
    private PriceRange priceRange;
    private Boolean isPrescriptionOnly;
    private ActiveSubstanceFilter activeSubstances;
}

