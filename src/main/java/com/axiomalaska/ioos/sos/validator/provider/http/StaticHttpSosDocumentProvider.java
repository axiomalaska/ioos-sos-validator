package com.axiomalaska.ioos.sos.validator.provider.http;

import java.net.URL;

import org.apache.http.client.methods.HttpGet;
import org.apache.xmlbeans.XmlObject;

import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.InvalidUrlException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;

public abstract class StaticHttpSosDocumentProvider extends AbstractHttpSosDocumentProvider{
    public StaticHttpSosDocumentProvider(URL url) throws InvalidUrlException {
        super(url);
    }
    
    protected XmlObject sendRequest(String suffix) throws SosValidationException, CompositeSosValidationException {
        return sendRequest(new HttpGet(url.toExternalForm() + suffix));
    }
}