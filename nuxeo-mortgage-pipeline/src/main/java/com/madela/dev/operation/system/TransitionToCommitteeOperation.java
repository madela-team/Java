package com.madela.dev.operation.system;

import com.madela.dev.model.MortgageStatus;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.core.api.CoreSession;

@Operation(id="Mortgage.TransitionToCommitteeOperation", category="Mortgage")
public class TransitionToCommitteeOperation extends AbstractTransitionOperation {
    @Context
    protected CoreSession session;

    @Override
    protected CoreSession getCoreSession() {
        return this.session;
    }

    @Override
    protected MortgageStatus getTargetStatus() {
        return MortgageStatus.COMMITTEE;
    }
}
