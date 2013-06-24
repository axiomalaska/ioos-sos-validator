package com.axiomalaska.ioos.sos.validator.provider.http;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlbeans.XmlObject;

import com.axiomalaska.ioos.sos.validator.config.InvalidUrlException;
import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;

public class IoosGoogleCodeProviderm1_0 extends StaticHttpSosDocumentProvider{
    public IoosGoogleCodeProviderm1_0() throws MalformedURLException, InvalidUrlException{
        super(new URL(SosConstants.IOOS_GOOGLE_CODE_M1_0_TEMPLATE_URL));
    }

    @Override
    protected XmlObject getDocumentXml(SosDocumentType document)
            throws SosValidationException, CompositeSosValidationException {
        switch(document){
            case M1_0_CAPABILITIES:
                return sendRequest("/SOS-GetCapabilities.xml");
            case M1_0_SENSOR_ML_NETWORK:
                return sendRequest("/SML-DescribeSensor-Network.xml");
            case M1_0_SENSOR_ML_STATION:
                return sendRequest("/SML-DescribeSensor-Station.xml");
            case M1_0_SENSOR_ML_SENSOR:
                break;
            case M1_0_OBSERVATION_COLLECTION_TIME_SERIES:
                break;
            case M1_0_OBSERVATION_COLLECTION_TIME_SERIES_PROFILE:
                break;            
        }
        return null;
    }
}