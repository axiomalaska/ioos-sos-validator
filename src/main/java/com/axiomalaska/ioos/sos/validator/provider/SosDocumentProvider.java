package com.axiomalaska.ioos.sos.validator.provider;

import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlObject;

import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.UnsupportedDocumentException;

public abstract class SosDocumentProvider {
    private final Map<SosDocumentType,XmlObject> cache = new HashMap<SosDocumentType,XmlObject>();

    public XmlObject getDocument(SosDocumentType document) throws SosValidationException, CompositeSosValidationException{
        if (cache.containsKey(document)) {
            return cache.get(document);
        }
        XmlObject xmlObject = getDocumentXml(document);
        if (xmlObject == null){
            throw new UnsupportedDocumentException(this, document);            
        }
        cache.put(document, xmlObject);
        return xmlObject;
    }

    protected abstract XmlObject getDocumentXml(SosDocumentType document) throws SosValidationException, CompositeSosValidationException;

    @Override
    public String toString(){
        return this.getClass().getSimpleName();
    }
}
