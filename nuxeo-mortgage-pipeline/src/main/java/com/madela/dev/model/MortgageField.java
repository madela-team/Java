package com.madela.dev.model;

public enum MortgageField {
    CREDIT_SCORE("appl", "creditScore"),
    INCOME("appl", "income"),
    REQUESTED_AMOUNT("mort", "requestedAmount"),
    RISK_LEVEL("mort","riskLevel"),
    STATUS("mort", "status");

    private final String prefix;
    private final String field;

    MortgageField(String prefix, String field) {
        this.prefix = prefix;
        this.field = field;
    }

    public String xpath() {
        return prefix + ":" + field;
    }
}
