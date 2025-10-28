package dev.madela.apteka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConcentrationFilter {
    private Float gt;
    private Float lt;
}