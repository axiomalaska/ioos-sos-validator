package com.axiomalaska.ioos.sos.validator.provider.http;

import java.io.UnsupportedEncodingException;
import java.net.URL;

import net.opengis.sos.x10.GetCapabilitiesDocument;
import net.opengis.sos.x10.GetCapabilitiesDocument.GetCapabilities;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.xmlbeans.XmlObject;

import com.axiomalaska.ioos.sos.validator.config.InvalidUrlException;
import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;

public class PoxHttpSosDocumentProvider extends AbstractHttpSosDocumentProvider{

    public PoxHttpSosDocumentProvider(URL url) throws InvalidUrlException {
        super(url);
    }
    
    protected XmlObject sendRequest(XmlObject xmlObject) throws SosValidationException, CompositeSosValidationException {
        HttpPost request = new HttpPost(url.toExternalForm());
        try {
            request.setEntity(new StringEntity(xmlObject.xmlText()));
        } catch (UnsupportedEncodingException e) {
            throw new SosValidationException(e);
        }        
        return sendRequest(request);        
    }
    
    protected XmlObject getCapabilitiesm1_0() throws SosValidationException, CompositeSosValidationException{
        GetCapabilitiesDocument xbGetCapabilitiesDoc = GetCapabilitiesDocument.Factory.newInstance();
        GetCapabilities xbGetCapabilities = xbGetCapabilitiesDoc.addNewGetCapabilities();
        xbGetCapabilities.setService(SosConstants.SOS);
        xbGetCapabilities.addNewAcceptVersions().addNewVersion().setStringValue(SosConstants.SOS_V1);
        return sendRequest(xbGetCapabilitiesDoc);
    }

    @Override
    protected XmlObject getDocumentXml(SosDocumentType document) throws SosValidationException, CompositeSosValidationException{
        switch(document){
            case M1_0_CAPABILITIES:
                return getCapabilitiesm1_0();
            case M1_0_SENSOR_ML_NETWORK:
                break;
            case M1_0_SENSOR_ML_STATION:
                break;
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
