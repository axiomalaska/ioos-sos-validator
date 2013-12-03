package com.axiomalaska.ioos.sos.validator.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeSosValidationException extends Exception{
    private static final long serialVersionUID = -8465456625013193861L;
    List<SosValidationException> exceptions = new ArrayList<SosValidationException>();
    
    public CompositeSosValidationException(){
        super(CompositeSosValidationException.class.getCanonicalName());
    }
    
    public void addException(SosValidationException exception) {
        exceptions.add(exception);
    }
    
    public List<SosValidationException> getExceptions() {
        return Collections.unmodifiableList(exceptions);
    }
    
    public boolean isEmpty() {
        return exceptions.isEmpty();
    }
    
    public String toString() {
        return exceptions.toString();
    }

    @Override
    public String getMessage() {
        return toString();
    }    
}
