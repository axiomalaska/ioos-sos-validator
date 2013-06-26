package com.axiomalaska.ioos.sos.validator.provider.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.opengis.om.x10.ObservationCollectionDocument;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.sos.x10.CapabilitiesDocument;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.apache.xmlbeans.XmlObject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.axiomalaska.ioos.sos.IoosSosConstants;
import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.InvalidRequestConfigurationException;
import com.axiomalaska.ioos.sos.validator.exception.InvalidUrlException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;
import com.axiomalaska.ioos.sos.validator.provider.http.config.GetObservationConstellation;
import com.axiomalaska.ioos.sos.validator.provider.http.config.RequestConfiguration;
import com.axiomalaska.ioos.sos.validator.util.XmlHelper;

public class KvpHttpSosDocumentProvider extends SosServerSosDocumentProvider{    
    public KvpHttpSosDocumentProvider(URL url, RequestConfiguration config)
            throws InvalidUrlException, MalformedURLException, InvalidRequestConfigurationException {
        super(url, config);
    }

    protected List<NameValuePair> getBaseQueryParams() {
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair(SosConstants.SERVICE, SosConstants.SOS));
        return qparams;
    }    
    
    @Override
    protected CapabilitiesDocument getCapabilitiesm1_0() throws SosValidationException, CompositeSosValidationException{
        List<NameValuePair> kvps = getBaseQueryParams();
        kvps.add(new BasicNameValuePair(SosConstants.REQUEST, SosConstants.GET_CAPABILITIES));
        kvps.add(new BasicNameValuePair(SosConstants.ACCEPT_VERSIONS, SosConstants.SOS_V1));
        XmlObject result = sendRequest(new HttpGet(getUrl(kvps)));
        return XmlHelper.castResult(this, result, CapabilitiesDocument.class, SosDocumentType.M1_0_CAPABILITIES, CapabilitiesDocument.type);        
    }

    @Override
    protected SensorMLDocument describeSensorm1_0(String procedure, SosDocumentType docType) throws SosValidationException, CompositeSosValidationException{
        if (procedure == null) {
            return null;
        }

        List<NameValuePair> kvps = getBaseQueryParams();
        kvps.add(new BasicNameValuePair(SosConstants.REQUEST, SosConstants.DESCRIBE_SENSOR));
        kvps.add(new BasicNameValuePair(SosConstants.VERSION, SosConstants.SOS_V1));
        kvps.add(new BasicNameValuePair(SosConstants.PROCEDURE, procedure));
        kvps.add(new BasicNameValuePair(SosConstants.OUTPUT_FORMAT, IoosSosConstants.SML_PROFILE_M10));
        XmlObject result = sendRequest(new HttpGet(getUrl(kvps)));
        return XmlHelper.castResult(this, result, SensorMLDocument.class, docType, SensorMLDocument.type);
    }

    @Override
    protected ObservationCollectionDocument getObservationm1_0(
            GetObservationConstellation constellation, SosDocumentType docType)
            throws SosValidationException, CompositeSosValidationException {
        List<NameValuePair> kvps = getBaseQueryParams();
        kvps.add(new BasicNameValuePair(SosConstants.REQUEST, SosConstants.GET_OBSERVATION));
        kvps.add(new BasicNameValuePair(SosConstants.VERSION, SosConstants.SOS_V1));        
        kvps.add(new BasicNameValuePair(SosConstants.OFFERING, constellation.getOffering()));
        kvps.add(new BasicNameValuePair(SosConstants.PROCEDURE,
                StringUtils.join(constellation.getProcedures(), ",")));
        kvps.add(new BasicNameValuePair(SosConstants.OBSERVED_PROPERTY,
                StringUtils.join(constellation.getObservedProperties(), ",")));
        
        if (constellation.getStartTime() != null || constellation.getEndTime() != null) {
            StringBuilder timeString = new StringBuilder();
            if (constellation.getStartTime() != null) {
                timeString.append(constellation.getStartTime().toString());
            } else {
                timeString.append(DEFAULT_START_TIME.toString());
            }
            timeString.append("/");
            if (constellation.getEndTime() != null) {
                timeString.append(constellation.getEndTime().toString());
            } else {
                timeString.append(new DateTime(DateTimeZone.UTC).toString());
            }
            kvps.add(new BasicNameValuePair(SosConstants.EVENT_TIME, timeString.toString()));
        }
        
        kvps.add(new BasicNameValuePair(SosConstants.RESPONSE_FORMAT, IoosSosConstants.OM_PROFILE_M10));

        XmlObject result = sendRequest(new HttpGet(getUrl(kvps)));
        return XmlHelper.castResult(this, result, ObservationCollectionDocument.class, docType, ObservationCollectionDocument.type);
    }
}
