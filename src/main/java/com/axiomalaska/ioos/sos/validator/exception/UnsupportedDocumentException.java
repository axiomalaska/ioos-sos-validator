package com.axiomalaska.ioos.sos.validator.exception;

import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;

public class UnsupportedDocumentException extends SosValidationException{
    private static final long serialVersionUID = 2318727281749007447L;

    public UnsupportedDocumentException(SosDocumentProvider provider, SosDocumentType doc){
        this(Severity.FATAL, provider, doc);
    }

    public UnsupportedDocumentException(Severity severity, SosDocumentProvider provider,
            SosDocumentType doc){
        super(severity, doc.name() + " is not supported by provider " + provider.toString());
    }
}
