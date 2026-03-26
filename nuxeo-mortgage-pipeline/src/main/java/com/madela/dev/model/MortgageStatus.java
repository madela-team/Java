package com.madela.dev.model;

public enum MortgageStatus {
    DRAFT("draft", "none"),
    SUBMITTED("submitted", "to_submitted"),
    SCORING("scoring", "to_scoring"),
    COMMITTEE("committee", "to_committee"),
    APPROVED("approved", "to_approved"),
    REJECTED("rejected", "to_rejected");

    private final String name;
    private final String transitionStep;

    MortgageStatus(String name, String transitionStep) {
        this.name = name;
        this.transitionStep = transitionStep;
    }

    public String getName() {
        return name;
    }

    public String getTransitionStep() {
        return transitionStep;
    }
}
