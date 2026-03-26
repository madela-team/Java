package com.madela.dev.validator;

import org.nuxeo.ecm.core.api.DocumentModel;

public interface AttachmentValidator {
    boolean isValid(DocumentModel doc);
}
