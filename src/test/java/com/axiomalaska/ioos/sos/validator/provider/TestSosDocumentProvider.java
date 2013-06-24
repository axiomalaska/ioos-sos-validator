package com.axiomalaska.ioos.sos.validator.provider;

import java.io.IOException;
import java.io.InputStream;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;

public class TestSosDocumentProvider extends SosDocumentProvider{    
    @Override
    protected XmlObject getDocumentXml(SosDocumentType document) throws SosValidationException{
        InputStream resourceStream = this.getClass().getClassLoader().getResourceAsStream("documents/" + document.name() + ".xml");
        if (resourceStream != null){
            try {
                return XmlObject.Factory.parse(resourceStream);
            } catch (XmlException e) {
                throw new SosValidationException(e);
            } catch (IOException e) {
                throw new SosValidationException(e);
            }
        }
        return null;
    }
}
