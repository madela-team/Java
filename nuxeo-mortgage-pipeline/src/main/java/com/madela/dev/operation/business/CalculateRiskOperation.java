package com.madela.dev.operation.business;

import com.madela.dev.model.MortgageField;
import com.madela.dev.service.MortgageRiskService;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.DocumentModel;

@Operation(id = "Mortgage.CalculateRiskOperation", category = "Mortgage")
public class CalculateRiskOperation {

    private final MortgageRiskService riskService = new MortgageRiskService();

    @OperationMethod
    public DocumentModel run(DocumentModel doc) {
        Long score = (Long) doc.getPropertyValue(MortgageField.CREDIT_SCORE.xpath());
        Double amount = (Double) doc.getPropertyValue(MortgageField.REQUESTED_AMOUNT.xpath());
        Double income = (Double) doc.getPropertyValue(MortgageField.INCOME.xpath());

        String riskLevel = riskService.calculateRisk(score, amount, income);
        doc.setPropertyValue(MortgageField.RISK_LEVEL.xpath(), riskLevel);
        doc.getCoreSession().save();
        return doc;
    }
}