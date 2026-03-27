package com.madela.dev.validator.impl;

import com.madela.dev.validator.AttachmentValidator;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RequiredDocumentsValidator implements AttachmentValidator {
    public static final String PASSPORT = "passport";
    public static final String SNILS = "snils";
    public static final String NDFL = "2ndfl";

    private static final List<String> REQUIRED_FILES = Arrays.asList(PASSPORT, SNILS, NDFL);

    @Override
    public boolean isValid(DocumentModel doc) {
        List<Map<String, Object>> files = (List<Map<String, Object>>) doc.getPropertyValue("files:files");
        if (files == null || files.isEmpty()) return false;

        return REQUIRED_FILES.stream().allMatch(req ->
                files.stream()
                        .map(file -> file != null ? (Blob) file.get("file") : null)
                        .anyMatch(blob -> blob != null && blob.getFilename() != null && blob.getFilename().toLowerCase().contains(req))
        );
    }
}
