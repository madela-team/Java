package com.madela.dev.listener;

import com.madela.dev.model.MortgageStatus;
import com.madela.dev.validator.impl.RequiredDocumentsValidator;
import org.nuxeo.ecm.core.api.DocumentModel;

public class MortgageDraftToSubmittedListener extends AbstractMortgageListener {
    public static final String CHANGE_STATE_FROM_DRAFT_TO_SUBMITTED_CHAIN = "Mortgage.ChangeStateFromDraftToSubmittedChain";

    private final RequiredDocumentsValidator validator = new RequiredDocumentsValidator();

    @Override
    protected void processEvent(DocumentModel doc) {
        if (MortgageStatus.DRAFT.getName().equals(doc.getCurrentLifeCycleState()) && validator.isValid(doc)) {
            runChain(doc, CHANGE_STATE_FROM_DRAFT_TO_SUBMITTED_CHAIN);
        }
    }
}