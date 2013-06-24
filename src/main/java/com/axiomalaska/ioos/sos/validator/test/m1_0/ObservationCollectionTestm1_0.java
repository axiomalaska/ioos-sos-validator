package com.axiomalaska.ioos.sos.validator.test.m1_0;

import net.opengis.om.x10.ObservationCollectionDocument;

import org.apache.xmlbeans.XmlObject;
import org.junit.Before;

import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.InvalidSosDocumentException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;
import com.axiomalaska.ioos.sos.validator.test.AbstractSensorMLTest;

public class ObservationCollectionTestm1_0 extends AbstractSensorMLTest{
    public ObservationCollectionTestm1_0(SosDocumentProvider provider) {
        super(provider);
    }

    private static ObservationCollectionDocument xbObservationCollection;

    @Before
    public void setUp() throws SosValidationException, CompositeSosValidationException{
        XmlObject xmlObject = provider.getDocument(SosDocumentType.M1_0_OBSERVATION_COLLECTION);
        if (!(xmlObject instanceof ObservationCollectionDocument)){
            throw new InvalidSosDocumentException(provider, SosDocumentType.M1_0_OBSERVATION_COLLECTION,
                    ObservationCollectionDocument.type);
        }
        xbObservationCollection = (ObservationCollectionDocument) xmlObject;
    }

    @Override
    public SosDocumentType getSosDocumentType() {
        return SosDocumentType.M1_0_OBSERVATION_COLLECTION;
    }

    @Override
    protected XmlObject getSosDocument() {
        return xbObservationCollection;
    }
}