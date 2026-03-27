package com.madela.dev.model;

public enum MortgageRiskLevel {
    LOW("Низкий"),
    MEDIUM("Средний"),
    HIGH("Высокий"),
    UNKNOWN("Неизвестно");

    private final String value;

    MortgageRiskLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
