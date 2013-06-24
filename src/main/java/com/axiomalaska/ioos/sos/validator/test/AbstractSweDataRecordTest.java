package com.axiomalaska.ioos.sos.validator.test;

import net.opengis.swe.x20.DataRecordDocument;

import org.apache.xmlbeans.XmlObject;
import org.junit.Before;

import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.util.XmlHelper;

public abstract class AbstractSweDataRecordTest extends AbstractSosValidationTest{
    protected static DataRecordDocument xbDataRecordDocument;

    public AbstractSweDataRecordTest(SosDocumentProvider provider) {
        super(provider);
    }

    @Before
    public void setUp() throws SosValidationException, CompositeSosValidationException{
        xbDataRecordDocument = XmlHelper.castResult(provider, provider.getDocument(getSosDocumentType()),
                DataRecordDocument.class, getSosDocumentType(), DataRecordDocument.type);
    }

    @Override
    protected XmlObject getSosDocument() {
        return xbDataRecordDocument;
    }    
}