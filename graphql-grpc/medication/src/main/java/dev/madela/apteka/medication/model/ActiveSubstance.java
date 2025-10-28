package dev.madela.apteka.medication.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ActiveSubstance {
    private UUID id;
    private UUID medicationId;
    private String name;
    private double concentration;
}
