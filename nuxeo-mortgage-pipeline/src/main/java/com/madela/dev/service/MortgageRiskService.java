package com.madela.dev.service;

import com.madela.dev.model.MortgageRiskLevel;

public class MortgageRiskService {
    public String calculateRisk(Long creditScore, Double amount, Double income) {
        if (creditScore == null || amount == null || income == null || amount == 0) {
            return MortgageRiskLevel.UNKNOWN.getValue();
        }

        if (creditScore >= 750 && (income / amount) > 0.3) return MortgageRiskLevel.LOW.getValue();
        if (creditScore >= 600) return MortgageRiskLevel.MEDIUM.getValue();
        return MortgageRiskLevel.HIGH.getValue();
    }
}
