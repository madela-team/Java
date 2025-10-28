package dev.madela.apteka.medication.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryStock {
    private String category;
    private int count;
}
