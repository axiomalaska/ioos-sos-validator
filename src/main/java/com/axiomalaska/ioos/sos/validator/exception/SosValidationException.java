package com.axiomalaska.ioos.sos.validator.exception;


public class SosValidationException extends Exception{
    private static final long serialVersionUID = -606961866248158184L;
    private Severity severity;
    
    public static enum Severity{
        FATAL, WARNING, INFO
    }

    public SosValidationException(String message){
        this(Severity.FATAL, message);
    }

    public SosValidationException(Severity severity, String message){
        super(message);
        this.severity = severity;
    }
    
    public SosValidationException(Throwable t){
        this(Severity.FATAL, t);
    }

    public SosValidationException(Severity severity, Throwable t){
        super(t);
        this.severity = severity;
    }
    
    public Severity getSeverity() {
        return severity;
    }
}