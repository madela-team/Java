package com.madela.dev.operation.system;

import com.madela.dev.model.MortgageField;
import com.madela.dev.model.MortgageStatus;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public abstract class AbstractTransitionOperation {
    protected abstract CoreSession getCoreSession();
    protected abstract MortgageStatus getTargetStatus();

    @OperationMethod
    public DocumentModel run(DocumentModel doc) {
        doc.followTransition(getTargetStatus().getTransitionStep());
        doc.setPropertyValue(MortgageField.STATUS.xpath(), getTargetStatus().getName());
        return getCoreSession().saveDocument(doc);
    }
}
