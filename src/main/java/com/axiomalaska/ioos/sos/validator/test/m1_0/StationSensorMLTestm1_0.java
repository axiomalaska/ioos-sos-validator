package com.axiomalaska.ioos.sos.validator.test.m1_0;

import net.opengis.sensorML.x101.SensorMLDocument;

import org.apache.xmlbeans.XmlObject;
import org.junit.Before;

import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.InvalidSosDocumentException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;
import com.axiomalaska.ioos.sos.validator.test.AbstractSensorMLTest;

public class StationSensorMLTestm1_0 extends AbstractSensorMLTest{
    public StationSensorMLTestm1_0(SosDocumentProvider provider) {
        super(provider);
    }

    private static SensorMLDocument xbSensorML;

    @Before
    public void setUp() throws SosValidationException, CompositeSosValidationException{
        XmlObject xmlObject = provider.getDocument(SosDocumentType.M1_0_SENSOR_ML_STATION);
        if (!(xmlObject instanceof SensorMLDocument)){
            throw new InvalidSosDocumentException(provider, SosDocumentType.M1_0_SENSOR_ML_STATION,
                    SensorMLDocument.type);
        }
        xbSensorML = (SensorMLDocument) xmlObject;
    }

    @Override
    public SosDocumentType getSosDocumentType() {
        return SosDocumentType.M1_0_SENSOR_ML_STATION;
    }

    @Override
    protected XmlObject getSosDocument() {
        return xbSensorML;
    }
}