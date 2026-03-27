package com.madela.dev.listener;

import com.madela.dev.model.MortgageField;
import com.madela.dev.model.MortgageStatus;
import org.nuxeo.ecm.core.api.DocumentModel;

public class MortgageSubmittedToScoringListener extends AbstractMortgageListener {
    public static final String CHANGE_STATE_FROM_SUBMITTED_TO_SCORING_CHAIN = "Mortgage.ChangeStateFromSubmittedToScoringChain";

    @Override
    protected void processEvent(DocumentModel doc) {
        if (!MortgageStatus.SUBMITTED.getName().equals(doc.getCurrentLifeCycleState())) {
            return;
        }

        if (isScoringDataNotPresent(doc)) {
            return;
        }
        runChain(doc, CHANGE_STATE_FROM_SUBMITTED_TO_SCORING_CHAIN);
    }

    private boolean isScoringDataNotPresent(DocumentModel doc) {
        return doc.getPropertyValue(MortgageField.CREDIT_SCORE.xpath()) == null
                && doc.getPropertyValue(MortgageField.REQUESTED_AMOUNT.xpath()) == null
                && doc.getPropertyValue(MortgageField.INCOME.xpath()) == null;
    }
}