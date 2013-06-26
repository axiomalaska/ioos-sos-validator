package com.axiomalaska.ioos.sos.validator.provider.http;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import net.opengis.gml.TimeIndeterminateValueType;
import net.opengis.gml.TimePeriodType;
import net.opengis.gml.TimePositionType;
import net.opengis.ogc.BinaryTemporalOpType;
import net.opengis.om.x10.ObservationCollectionDocument;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.sos.x10.CapabilitiesDocument;
import net.opengis.sos.x10.DescribeSensorDocument;
import net.opengis.sos.x10.DescribeSensorDocument.DescribeSensor;
import net.opengis.sos.x10.GetCapabilitiesDocument;
import net.opengis.sos.x10.GetCapabilitiesDocument.GetCapabilities;
import net.opengis.sos.x10.GetObservationDocument;
import net.opengis.sos.x10.GetObservationDocument.GetObservation;
import net.opengis.sos.x10.GetObservationDocument.GetObservation.EventTime;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.xmlbeans.XmlObject;

import com.axiomalaska.ioos.sos.IoosSosConstants;
import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.InvalidRequestConfigurationException;
import com.axiomalaska.ioos.sos.validator.exception.InvalidUrlException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;
import com.axiomalaska.ioos.sos.validator.provider.http.config.GetObservationConstellation;
import com.axiomalaska.ioos.sos.validator.provider.http.config.RequestConfiguration;
import com.axiomalaska.ioos.sos.validator.util.XmlHelper;

public class PoxHttpSosDocumentProvider extends SosServerSosDocumentProvider{
    private static final String NS_OGC = "http://www.opengis.net/ogc";
    private static final String OGC = "ogc";
    
    private static final String NS_GML = "http://www.opengis.net/gml";
    private static final String GML = "gml";
    
    public PoxHttpSosDocumentProvider(URL url, RequestConfiguration config)
                throws InvalidUrlException, MalformedURLException, InvalidRequestConfigurationException {
        super(url, config);
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

    @Override
    protected CapabilitiesDocument getCapabilitiesm1_0() throws SosValidationException, CompositeSosValidationException{
        GetCapabilitiesDocument xbGetCapabilitiesDoc = GetCapabilitiesDocument.Factory.newInstance();
        GetCapabilities xbGetCapabilities = xbGetCapabilitiesDoc.addNewGetCapabilities();
        xbGetCapabilities.setService(SosConstants.SOS);
        xbGetCapabilities.addNewAcceptVersions().addNewVersion().setStringValue(SosConstants.SOS_V1);
        XmlObject result = sendRequest(xbGetCapabilitiesDoc);
        return XmlHelper.castResult(this, result, CapabilitiesDocument.class, SosDocumentType.M1_0_CAPABILITIES, CapabilitiesDocument.type);
    }

    @Override
    protected SensorMLDocument describeSensorm1_0(String procedure, SosDocumentType docType)
            throws SosValidationException, CompositeSosValidationException {
        DescribeSensorDocument xbDescribeSensorDoc = DescribeSensorDocument.Factory.newInstance();
        DescribeSensor xbDescribeSensor = xbDescribeSensorDoc.addNewDescribeSensor();
        xbDescribeSensor.setService(SosConstants.SOS);
        xbDescribeSensor.setVersion(SosConstants.SOS_V1);
        xbDescribeSensor.setOutputFormat(IoosSosConstants.SML_PROFILE_M10);
        xbDescribeSensor.setProcedure(procedure);
        XmlObject result = sendRequest(xbDescribeSensorDoc);
        return XmlHelper.castResult(this, result, SensorMLDocument.class, docType, SensorMLDocument.type);
    }

    @Override
    protected ObservationCollectionDocument getObservationm1_0(
            GetObservationConstellation constellation, SosDocumentType docType)
            throws SosValidationException, CompositeSosValidationException {
        GetObservationDocument xbGetObsDoc = GetObservationDocument.Factory.newInstance();
        GetObservation xbGetObs = xbGetObsDoc.addNewGetObservation();
        xbGetObs.setService(SosConstants.SOS);
        xbGetObs.setVersion(SosConstants.SOS_V1);
        xbGetObs.setOffering(constellation.getOffering());
        for (String procedure : constellation.getProcedures()) {
            xbGetObs.addProcedure(procedure);
        }
        for (String observedProperty : constellation.getObservedProperties()) {
            xbGetObs.addObservedProperty(observedProperty);
        }
        if (constellation.getStartTime() != null || constellation.getEndTime() != null) {
            EventTime xbEventTime = xbGetObs.addNewEventTime();
            BinaryTemporalOpType xbTmDuring = (BinaryTemporalOpType) xbEventTime.addNewTemporalOps()
                    .substitute(new QName(NS_OGC, "TM_During", OGC), BinaryTemporalOpType.type);
            xbTmDuring.addNewPropertyName().newCursor().setTextValue("timeFilter");

            TimePeriodType xbTimePeriod = (TimePeriodType) xbTmDuring.addNewTimeObject()
                    .substitute(new QName(NS_GML, "TimePeriod", GML), TimePeriodType.type);
            TimePositionType xbBegin = xbTimePeriod.addNewBeginPosition();
            TimePositionType xbEnd = xbTimePeriod.addNewEndPosition();
                        
            if (constellation.getStartTime() != null){
                xbBegin.setStringValue(constellation.getStartTime().toString());    
            } else {
                xbBegin.setStringValue(DEFAULT_START_TIME.toString());
            }
            
            if (constellation.getEndTime() != null){
                xbEnd.setStringValue(constellation.getEndTime().toString());
            } else {
                xbEnd.setIndeterminatePosition(TimeIndeterminateValueType.NOW);
            }
        }
        xbGetObs.setResponseFormat(IoosSosConstants.OM_PROFILE_M10);
        XmlObject result = sendRequest(xbGetObsDoc);
        return XmlHelper.castResult(this, result, ObservationCollectionDocument.class, docType, ObservationCollectionDocument.type);
    }
}
