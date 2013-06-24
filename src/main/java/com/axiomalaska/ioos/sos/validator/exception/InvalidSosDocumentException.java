package com.axiomalaska.ioos.sos.validator.exception;

import org.apache.xmlbeans.SchemaType;

import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;

public class InvalidSosDocumentException extends SosValidationException{
    private static final long serialVersionUID = -2367826874651476592L;

    public InvalidSosDocumentException(SosDocumentProvider provider, SosDocumentType targetDocType,
            SchemaType targetSchemaType, SchemaType actualSchemaType){
        this(Severity.FATAL, provider, targetDocType, targetSchemaType, actualSchemaType);
    }

    public InvalidSosDocumentException(Severity severity, SosDocumentProvider provider,
            SosDocumentType targetDocType, SchemaType targetSchemaType, SchemaType actualSchemaType ){
        super(Severity.FATAL, targetDocType.name() + " provided by SosDocumentProvider " + provider.toString()
                + " is not a valid instance of " + targetSchemaType.toString() + " (acutal schema type is "
                + actualSchemaType.toString() + ")");
    }
}
