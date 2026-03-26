package com.madela.dev.listener;

import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;

public abstract class AbstractMortgageListener implements EventListener {
    public static final String MORTGAGE_APPLICATION_DOC_TYPE = "MortgageApplication";
    public static final String DOCUMENT_MODIFIED_EVENT_TYPE = "documentModified";

    @Override
    public void handleEvent(Event event) {
        if (!DOCUMENT_MODIFIED_EVENT_TYPE.equals(event.getName())) {
            return;
        }
        if (!(event.getContext() instanceof DocumentEventContext)) {
            return;
        }

        DocumentEventContext ctx = (DocumentEventContext) event.getContext();
        DocumentModel doc = ctx.getSourceDocument();

        if (!MORTGAGE_APPLICATION_DOC_TYPE.equals(doc.getType())) {
            return;
        }
        processEvent(doc);
    }

    protected abstract void processEvent(DocumentModel doc);

    protected void runChain(DocumentModel doc, String chainId) {
        AutomationService automationService = Framework.getService(AutomationService.class);
        try (OperationContext opCtx = new OperationContext(doc.getCoreSession())) {
            opCtx.setInput(doc);
            automationService.run(opCtx, chainId);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при вызове цепочки: " + chainId, e);
        }
    }
}
