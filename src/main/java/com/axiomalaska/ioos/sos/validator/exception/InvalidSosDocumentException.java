package com.axiomalaska.ioos.sos.validator.exception;

import org.apache.xmlbeans.SchemaType;

import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;

public class InvalidSosDocumentException extends SosValidationException{
    private static final long serialVersionUID = -2367826874651476592L;

    public InvalidSosDocumentException(SosDocumentProvider provider,
            SosDocumentType docType, SchemaType schemaType){
        this(Severity.FATAL, provider, docType, schemaType);
    }

    public InvalidSosDocumentException(Severity severity, SosDocumentProvider provider,
            SosDocumentType docType, SchemaType schemaType){
        super(Severity.FATAL, docType.name() + " provided by SosDocumentProvider " + provider.toString()
                + " is not a valid instance of " + schemaType.toString());
    }
}
