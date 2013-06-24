package com.axiomalaska.ioos.sos.validator.exception;

public class SosHttpRequestException extends SosValidationException{
    private static final long serialVersionUID = 8644635988876693532L;

    public SosHttpRequestException(String message){
        this(Severity.FATAL, message);
    }

    public SosHttpRequestException(Severity severity, String message){
        super(severity, message);
    }
}
