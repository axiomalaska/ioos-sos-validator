package com.axiomalaska.ioos.sos.validator.provider.http;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.apache.xmlbeans.XmlObject;

import com.axiomalaska.ioos.sos.validator.config.InvalidUrlException;
import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;

public class KvpHttpSosDocumentProvider extends AbstractHttpSosDocumentProvider{

    public KvpHttpSosDocumentProvider(URL url) throws InvalidUrlException {
        super(url);
    }

    protected List<NameValuePair> getBaseQueryParams() {
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair(SosConstants.SERVICE, SosConstants.SOS));
        return qparams;
    }    
    
    protected XmlObject getCapabilitiesm1_0() throws SosValidationException, CompositeSosValidationException{
        List<NameValuePair> kvps = getBaseQueryParams();
        kvps.add(new BasicNameValuePair(SosConstants.REQUEST, SosConstants.GET_CAPABILITIES));
        kvps.add(new BasicNameValuePair(SosConstants.ACCEPT_VERSIONS, SosConstants.SOS_V1));
        return sendRequest(new HttpGet(getUrl(kvps)));
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
            case M1_0_OBSERVATION_COLLECTION:
                break;
            case M1_0_SWE_TIME_SERIES:
                break;
            case M1_0_SWE_TIME_SERIES_PROFILE:
                break;                
        }
        return null;
    }

}
