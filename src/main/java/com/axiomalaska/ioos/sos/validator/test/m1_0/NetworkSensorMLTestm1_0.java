package com.axiomalaska.ioos.sos.validator.test.m1_0;

import net.opengis.sensorML.x101.SensorMLDocument;

import org.apache.xmlbeans.XmlObject;
import org.junit.Before;

import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;
import com.axiomalaska.ioos.sos.validator.test.AbstractSensorMLTest;
import com.axiomalaska.ioos.sos.validator.util.XmlHelper;

public class NetworkSensorMLTestm1_0 extends AbstractSensorMLTest{
    public NetworkSensorMLTestm1_0(SosDocumentProvider provider) {
        super(provider);
    }

    private static SensorMLDocument xbSensorML;

    @Before
    public void setUp() throws SosValidationException, CompositeSosValidationException{
        xbSensorML = XmlHelper.castResult(provider, provider.getDocument(SosDocumentType.M1_0_SENSOR_ML_NETWORK),
                SensorMLDocument.class, SosDocumentType.M1_0_SENSOR_ML_NETWORK, SensorMLDocument.type);
    }

    @Override
    public SosDocumentType getSosDocumentType() {
        return SosDocumentType.M1_0_SENSOR_ML_NETWORK;
    }

    @Override
    protected XmlObject getSosDocument() {
        return xbSensorML;
    }
}