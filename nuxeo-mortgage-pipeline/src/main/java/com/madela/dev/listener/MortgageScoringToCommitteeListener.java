package com.madela.dev.listener;

import com.madela.dev.model.MortgageField;
import com.madela.dev.model.MortgageStatus;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;

public class MortgageScoringToCommitteeListener extends AbstractMortgageListener {
    public static final String CHANGE_STATE_FROM_SCORING_TO_COMMITTEE_CHAIN = "Mortgage.ChangeStateFromScoringToCommitteeChain";

    @Override
    protected void processEvent(DocumentModel doc) {
        if (!MortgageStatus.SCORING.getName().equals(doc.getCurrentLifeCycleState())) {
            return;
        }

        if (hasRequiredFields(doc)) {
            return;
        }
        runChain(doc, CHANGE_STATE_FROM_SCORING_TO_COMMITTEE_CHAIN);
    }

    private boolean hasRequiredFields(DocumentModel doc) {
        return doc.getPropertyValue(MortgageField.CREDIT_SCORE.xpath()) == null
                || doc.getPropertyValue(MortgageField.REQUESTED_AMOUNT.xpath()) == null
                || doc.getPropertyValue(MortgageField.INCOME.xpath()) == null;
    }
}