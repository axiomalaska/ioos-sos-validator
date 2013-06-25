package com.axiomalaska.ioos.sos.validator.provider.http.config;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.axiomalaska.ioos.sos.validator.xstream.XStreamRepository;
import com.axiomalaska.phenomena.Phenomena;
import com.axiomalaska.phenomena.UnitCreationException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("requestConfiguration")
public class RequestConfiguration {
    private String networkSmlProcedure;
    private String stationSmlProcedure;
    private String sensorSmlProcedure;
    private GetObservationConstellation timeSeriesConstellation;
    private GetObservationConstellation timeSeriesProfileConstellation;

    public String getNetworkSmlProcedure() {
        return networkSmlProcedure;
    }

    public void setNetworkSmlProcedure(String networkSmlProcedure) {
        this.networkSmlProcedure = networkSmlProcedure;
    }

    public String getStationSmlProcedure() {
        return stationSmlProcedure;
    }

    public void setStationSmlProcedure(String stationSmlProcedure) {
        this.stationSmlProcedure = stationSmlProcedure;
    }

    public String getSensorSmlProcedure() {
        return sensorSmlProcedure;
    }

    public void setSensorSmlProcedure(String sensorSmlProcedure) {
        this.sensorSmlProcedure = sensorSmlProcedure;
    }

    public GetObservationConstellation getTimeSeriesConstellation() {
        return timeSeriesConstellation;
    }

    public void setTimeSeriesConstellation(
            GetObservationConstellation timeSeriesConstellation) {
        this.timeSeriesConstellation = timeSeriesConstellation;
    }

    public GetObservationConstellation getTimeSeriesProfileConstellation() {
        return timeSeriesProfileConstellation;
    }

    public void setTimeSeriesProfileConstellation(
            GetObservationConstellation timeSeriesProfileConstellation) {
        this.timeSeriesProfileConstellation = timeSeriesProfileConstellation;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((networkSmlProcedure == null) ? 0 : networkSmlProcedure
                        .hashCode());
        result = prime
                * result
                + ((sensorSmlProcedure == null) ? 0 : sensorSmlProcedure
                        .hashCode());
        result = prime
                * result
                + ((stationSmlProcedure == null) ? 0 : stationSmlProcedure
                        .hashCode());
        result = prime
                * result
                + ((timeSeriesConstellation == null) ? 0
                        : timeSeriesConstellation.hashCode());
        result = prime
                * result
                + ((timeSeriesProfileConstellation == null) ? 0
                        : timeSeriesProfileConstellation.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RequestConfiguration other = (RequestConfiguration) obj;
        if (networkSmlProcedure == null) {
            if (other.networkSmlProcedure != null)
                return false;
        } else if (!networkSmlProcedure.equals(other.networkSmlProcedure))
            return false;
        if (sensorSmlProcedure == null) {
            if (other.sensorSmlProcedure != null)
                return false;
        } else if (!sensorSmlProcedure.equals(other.sensorSmlProcedure))
            return false;
        if (stationSmlProcedure == null) {
            if (other.stationSmlProcedure != null)
                return false;
        } else if (!stationSmlProcedure.equals(other.stationSmlProcedure))
            return false;
        if (timeSeriesConstellation == null) {
            if (other.timeSeriesConstellation != null)
                return false;
        } else if (!timeSeriesConstellation
                .equals(other.timeSeriesConstellation))
            return false;
        if (timeSeriesProfileConstellation == null) {
            if (other.timeSeriesProfileConstellation != null)
                return false;
        } else if (!timeSeriesProfileConstellation
                .equals(other.timeSeriesProfileConstellation))
            return false;
        return true;
    }

    public static RequestConfiguration exampleConfig(){
        String offering = "urn:ioos:network:test:all";
        String prudhoe = "urn:ioos:station:test:prudhoe";
        String blighreef = "urn:ioos:station:test:blighreef";
        String nikiski = "urn:ioos:station:test:nikiski";
        String airTemp = null;
        String seaWaterTemp = null;
        String windSpeed = null;
        String directionOfSeaWaterVelocity = null;
        String seaWaterSpeed = null;
        String seaWaterPracticalSalinity = null;
        try {
            airTemp = Phenomena.instance().AIR_TEMPERATURE.getId();
            seaWaterTemp = Phenomena.instance().SEA_WATER_TEMPERATURE.getId();
            windSpeed = Phenomena.instance().WIND_SPEED.getId();
            directionOfSeaWaterVelocity = Phenomena.instance().DIRECTION_OF_SEA_WATER_VELOCITY.getId();
            seaWaterSpeed = Phenomena.instance().SEA_WATER_SPEED.getId();
            seaWaterPracticalSalinity = Phenomena.instance().SEA_WATER_PRACTICAL_SALINITY.getId();
        } catch (UnitCreationException e) {
            e.printStackTrace();
        }
        
        RequestConfiguration config = new RequestConfiguration();
        config.setNetworkSmlProcedure(offering);
        config.setStationSmlProcedure(prudhoe);
        config.setSensorSmlProcedure("urn:ioos:sesnor:test:prudhoe:airtemp");
        
        GetObservationConstellation timeSeriesConstellation = new GetObservationConstellation();
        timeSeriesConstellation.setOffering(offering);
        timeSeriesConstellation.addProcedure(prudhoe);
        timeSeriesConstellation.addProcedure(blighreef);
        timeSeriesConstellation.addObservedProperty(airTemp);
        timeSeriesConstellation.addObservedProperty(seaWaterTemp);
        timeSeriesConstellation.addObservedProperty(windSpeed);
        timeSeriesConstellation.setStartTime(new DateTime("2010-02-11T00:00:00Z", DateTimeZone.UTC));
        timeSeriesConstellation.setEndTime(new DateTime("2013-08-03T00:00:00Z", DateTimeZone.UTC));
        
        config.setTimeSeriesConstellation(timeSeriesConstellation);

        GetObservationConstellation timeSeriesProfileConstellation = new GetObservationConstellation();
        timeSeriesProfileConstellation.setOffering(offering);
        timeSeriesProfileConstellation.addProcedure(blighreef);
        timeSeriesProfileConstellation.addProcedure(nikiski);        
        timeSeriesProfileConstellation.addObservedProperty(directionOfSeaWaterVelocity);
        timeSeriesProfileConstellation.addObservedProperty(seaWaterSpeed);
        timeSeriesProfileConstellation.addObservedProperty(seaWaterTemp);
        timeSeriesProfileConstellation.addObservedProperty(seaWaterPracticalSalinity);
        timeSeriesProfileConstellation.setStartTime(new DateTime("2012-01-01T00:00:00Z", DateTimeZone.UTC));        
        config.setTimeSeriesProfileConstellation(timeSeriesProfileConstellation);

        return config;
    }

    public static void main(String[] args) {
        System.out.println(XStreamRepository.instance().toXML(XStreamRepository.instance().fromXML(XStreamRepository.instance().toXML(exampleConfig()))));
    }
}
