package com.axiomalaska.ioos.sos.validator.exception;

import org.apache.xmlbeans.XmlValidationError;

public class SchemaValidationException extends SosValidationException{
    private static final long serialVersionUID = 1716425277972589608L;

    public SchemaValidationException(XmlValidationError xmlValidationError){
        this(Severity.FATAL, xmlValidationError);
    }

    public SchemaValidationException(Severity severity, XmlValidationError xmlValidationError){
        super(severity, xmlValidationError.toString());
    }
}
