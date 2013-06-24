package com.axiomalaska.ioos.sos.validator.provider;

import javax.xml.XMLConstants;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class XmlValidator {
    // javax.xml.validation.Validator for schema validating xml documents
    private static Validator xmlValidator;
    
    public static Validator getXmlValidator() throws SAXException {
        if (xmlValidator == null) {
            xmlValidator = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema().newValidator();
        }
        return xmlValidator;
    }
}