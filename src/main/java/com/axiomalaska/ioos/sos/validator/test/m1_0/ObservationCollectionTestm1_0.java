package com.axiomalaska.ioos.sos.validator.test.m1_0;

import net.opengis.om.x10.ObservationCollectionDocument;

import org.apache.xmlbeans.XmlObject;
import org.junit.Before;

import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;
import com.axiomalaska.ioos.sos.validator.test.AbstractSensorMLTest;
import com.axiomalaska.ioos.sos.validator.util.XmlHelper;

public class ObservationCollectionTestm1_0 extends AbstractSensorMLTest{
    public ObservationCollectionTestm1_0(SosDocumentProvider provider) {
        super(provider);
    }

    private static ObservationCollectionDocument xbObservationCollection;

    @Before
    public void setUp() throws SosValidationException, CompositeSosValidationException{
        xbObservationCollection = XmlHelper.castResult(provider, provider.getDocument(SosDocumentType.M1_0_OBSERVATION_COLLECTION),
                ObservationCollectionDocument.class, SosDocumentType.M1_0_OBSERVATION_COLLECTION, ObservationCollectionDocument.type);
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