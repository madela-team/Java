package dev.madela.apteka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveSubstanceFilter {
    private String name;
    private ConcentrationFilter concentration;
}