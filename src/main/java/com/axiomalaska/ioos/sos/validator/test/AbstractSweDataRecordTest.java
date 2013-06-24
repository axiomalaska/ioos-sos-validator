package com.axiomalaska.ioos.sos.validator.test;

import net.opengis.swe.x20.DataRecordDocument;

import org.apache.xmlbeans.XmlObject;
import org.junit.Before;

import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.InvalidSosDocumentException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;

public abstract class AbstractSweDataRecordTest extends AbstractSosValidationTest{
    protected static DataRecordDocument xbDataRecordDocument;

    public AbstractSweDataRecordTest(SosDocumentProvider provider) {
        super(provider);
    }
    
    @Before
    public void setUp() throws SosValidationException, CompositeSosValidationException{
        XmlObject xmlObject = provider.getDocument(SosDocumentType.M1_0_SWE_TIME_SERIES_PROFILE);
        if (!(xmlObject instanceof DataRecordDocument)){
            throw new InvalidSosDocumentException(provider, SosDocumentType.M1_0_SWE_TIME_SERIES_PROFILE,
                    DataRecordDocument.type);
        }
        xbDataRecordDocument = (DataRecordDocument) xmlObject;
    }

    @Override
    public SosDocumentType getSosDocumentType() {
        return SosDocumentType.M1_0_SWE_TIME_SERIES_PROFILE;
    }

    @Override
    protected XmlObject getSosDocument() {
        return xbDataRecordDocument;
    }    
}