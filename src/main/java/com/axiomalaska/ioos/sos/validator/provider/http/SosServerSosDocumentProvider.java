package com.axiomalaska.ioos.sos.validator.provider.http;

import java.net.URL;
import java.util.Set;

import net.opengis.om.x10.ObservationCollectionDocument;
import net.opengis.om.x10.ObservationType;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.sos.x10.CapabilitiesDocument;
import net.opengis.swe.x20.DataRecordDocument;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

import com.axiomalaska.ioos.sos.validator.config.InvalidUrlException;
import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.InvalidSosDocumentException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;

public abstract class SosServerSosDocumentProvider extends AbstractHttpSosDocumentProvider{
    protected String networkSmlProcedure;
    protected String stationSmlProcedure;
    protected String sensorSmlProcedure;
    protected GetObservationConstellation timeSeriesConstellation;
    protected GetObservationConstellation timeSeriesProfileConstellation;
    
    public SosServerSosDocumentProvider(URL url, String networkSmlProcedure, String stationSmlProcedure,
            String sensorSmlProcedure, GetObservationConstellation timeSeriesConstellation,
            GetObservationConstellation timeSeriesProfileConstellation) throws InvalidUrlException {        
        super(url);
        this.networkSmlProcedure = networkSmlProcedure;
        this.stationSmlProcedure = stationSmlProcedure;
        this.sensorSmlProcedure = sensorSmlProcedure;
        this.timeSeriesConstellation = timeSeriesConstellation;
        this.timeSeriesProfileConstellation = timeSeriesProfileConstellation;
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
                return describeSensorm1_0(networkSmlProcedure, SosDocumentType.M1_0_SENSOR_ML_NETWORK);
            case M1_0_SENSOR_ML_STATION:
                return describeSensorm1_0(stationSmlProcedure, SosDocumentType.M1_0_SENSOR_ML_STATION);
            case M1_0_SENSOR_ML_SENSOR:
                return describeSensorm1_0(sensorSmlProcedure, SosDocumentType.M1_0_SENSOR_ML_SENSOR);
            case M1_0_OBSERVATION_COLLECTION:
                return getObservationm1_0(timeSeriesConstellation, SosDocumentType.M1_0_OBSERVATION_COLLECTION);
            case M1_0_SWE_TIME_SERIES:
                getSweDataRecordFromObservationCollectionm1_0(timeSeriesConstellation, SosDocumentType.M1_0_SWE_TIME_SERIES);
            case M1_0_SWE_TIME_SERIES_PROFILE:
                getSweDataRecordFromObservationCollectionm1_0(timeSeriesProfileConstellation, SosDocumentType.M1_0_SWE_TIME_SERIES_PROFILE);
        }
        return null;
    }
}
