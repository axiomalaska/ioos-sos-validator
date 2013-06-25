package com.axiomalaska.ioos.sos.validator.provider.http;

import java.net.MalformedURLException;
import java.net.URL;

import net.opengis.om.x10.ObservationCollectionDocument;
import net.opengis.om.x10.ObservationType;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.sos.x10.CapabilitiesDocument;
import net.opengis.swe.x20.DataRecordDocument;

import org.apache.xmlbeans.XmlObject;
import org.joda.time.DateTime;

import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.InvalidRequestConfigurationException;
import com.axiomalaska.ioos.sos.validator.exception.InvalidUrlException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;
import com.axiomalaska.ioos.sos.validator.provider.http.config.GetObservationConstellation;
import com.axiomalaska.ioos.sos.validator.provider.http.config.RequestConfiguration;

public abstract class SosServerSosDocumentProvider extends AbstractHttpSosDocumentProvider{
    protected RequestConfiguration config;
    protected static final DateTime DEFAULT_START_TIME = new DateTime("1900-01-01T00:00:00Z");
    
    public SosServerSosDocumentProvider(URL url, RequestConfiguration config)
            throws InvalidUrlException, MalformedURLException, InvalidRequestConfigurationException {        
        super(url);
        verifyRequestConfig(config);
        this.config = config;
    }

    protected abstract CapabilitiesDocument getCapabilitiesm1_0() throws SosValidationException, CompositeSosValidationException;
    protected abstract SensorMLDocument describeSensorm1_0(String procedure, SosDocumentType docType) throws SosValidationException, CompositeSosValidationException;
    protected abstract ObservationCollectionDocument getObservationm1_0(GetObservationConstellation constellation, SosDocumentType docType)
            throws SosValidationException, CompositeSosValidationException;

    protected DataRecordDocument getSweDataRecordFromObservationCollectionm1_0(GetObservationConstellation constellation, 
            SosDocumentType docType) throws SosValidationException, CompositeSosValidationException {
        ObservationCollectionDocument xbObsCollection = getObservationm1_0(constellation, docType);
        if (xbObsCollection.getObservationCollection() == null) {
            throw new SosValidationException("ObservationCollection has no contents");
        }

        if (xbObsCollection.getObservationCollection().getMemberArray() == null
                || xbObsCollection.getObservationCollection().getMemberArray().length == 0) {
            throw new SosValidationException("ObservationCollection has no members");
        }

        if (xbObsCollection.getObservationCollection().getMemberArray(0).getObservation() == null) {
            throw new SosValidationException("First ObservationCollection member has no Observation");
        }
        
        ObservationType xbObservation = xbObsCollection.getObservationCollection().getMemberArray(0).getObservation();

        if (xbObservation.getResult() == null) {
            throw new SosValidationException("First ObservationCollection Observation has no result");
        }

        if (!(xbObservation.getResult() instanceof DataRecordDocument)) {
            throw new SosValidationException("First ObservationCollection Observation result is not a swe2:DataRecord");
        }
        
        return (DataRecordDocument) xbObservation.getResult();
    }
    
    @Override
    protected XmlObject getDocumentXml(SosDocumentType document) throws SosValidationException, CompositeSosValidationException{
        switch(document){
            case M1_0_CAPABILITIES:
                return getCapabilitiesm1_0();
            case M1_0_SENSOR_ML_NETWORK:
                return describeSensorm1_0(config.getNetworkSmlProcedure(), SosDocumentType.M1_0_SENSOR_ML_NETWORK);
            case M1_0_SENSOR_ML_STATION:
                return describeSensorm1_0(config.getStationSmlProcedure(), SosDocumentType.M1_0_SENSOR_ML_STATION);
            case M1_0_SENSOR_ML_SENSOR:
                return describeSensorm1_0(config.getSensorSmlProcedure(), SosDocumentType.M1_0_SENSOR_ML_SENSOR);
            case M1_0_OBSERVATION_COLLECTION:
                return getObservationm1_0(config.getTimeSeriesConstellation(), SosDocumentType.M1_0_OBSERVATION_COLLECTION);
            case M1_0_SWE_TIME_SERIES:
                getSweDataRecordFromObservationCollectionm1_0(config.getTimeSeriesConstellation(), SosDocumentType.M1_0_SWE_TIME_SERIES);
            case M1_0_SWE_TIME_SERIES_PROFILE:
                getSweDataRecordFromObservationCollectionm1_0(config.getTimeSeriesProfileConstellation(),
                        SosDocumentType.M1_0_SWE_TIME_SERIES_PROFILE);
        }
        return null;
    }
    
    private void verifyRequestConfig(RequestConfiguration c) throws InvalidRequestConfigurationException {
        if (c.getNetworkSmlProcedure() == null || c.getNetworkSmlProcedure().isEmpty()) {
            throw new InvalidRequestConfigurationException("networkSmlProcedure is required");
        }

        if (c.getStationSmlProcedure() == null || c.getStationSmlProcedure().isEmpty()) {
            throw new InvalidRequestConfigurationException("stationSmlProcedure is required");
        }

        if (c.getSensorSmlProcedure() == null || c.getSensorSmlProcedure().isEmpty()) {
            throw new InvalidRequestConfigurationException("sensorSmlProcedure is required");
        }

        if (c.getTimeSeriesConstellation() == null) {
            throw new InvalidRequestConfigurationException("timeSeriesConstellation is required");
        }        
        verifyGetObservationConstellation(c.getTimeSeriesConstellation());

        if (c.getTimeSeriesProfileConstellation() == null) {
            throw new InvalidRequestConfigurationException("timeSeriesProfileConstellation is required");
        }        
        verifyGetObservationConstellation(c.getTimeSeriesProfileConstellation());        
    }
    
    private void verifyGetObservationConstellation(GetObservationConstellation c)
            throws InvalidRequestConfigurationException {
        if (c.getOffering() == null || c.getOffering().isEmpty()) {
            throw new InvalidRequestConfigurationException("offering is required in GetObservationConstellation");
        }
        
        if (c.getProcedures().isEmpty()) {
            throw new InvalidRequestConfigurationException("At least one procedure is required in GetObservationConstellation");
        }        

        if (c.getObservedProperties().isEmpty()) {
            throw new InvalidRequestConfigurationException("At least one observedProperty is required in GetObservationConstellation");
        }        
        
    }
}
