package com.axiomalaska.ioos.sos.validator.exception;


public class InvalidRequestConfigurationException extends SosValidationException{
    private static final long serialVersionUID = -4312604994188830974L;

    public InvalidRequestConfigurationException(String message){
        this(Severity.FATAL, message);
    }

    public InvalidRequestConfigurationException(Severity severity, String message){
        super(Severity.FATAL, message);
    }
}
