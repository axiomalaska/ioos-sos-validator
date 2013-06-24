package com.axiomalaska.ioos.sos.validator.config;

public class InvalidUrlException extends Exception{
    private static final long serialVersionUID = -3396376124947845809L;

    public InvalidUrlException(String url) {
        super(url + " is not a valid URL");
    }
    
    public InvalidUrlException(String url, Throwable t) {
        super(url + " is not a valid URL", t);
    }
    
}