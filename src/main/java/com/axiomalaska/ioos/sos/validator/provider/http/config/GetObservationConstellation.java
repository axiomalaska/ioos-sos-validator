package com.axiomalaska.ioos.sos.validator.provider.http.config;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;

import com.axiomalaska.ioos.sos.validator.xstream.DateTimeConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("test")
public class GetObservationConstellation {
    private String offering;
    
    @XStreamImplicit(itemFieldName="procedure")
    private Set<String> procedures = new HashSet<String>();
    
    @XStreamImplicit(itemFieldName="observedProperty")
    private Set<String> observedProperties = new HashSet<String>();

    @XStreamConverter(DateTimeConverter.class)
    private DateTime startTime;

    @XStreamConverter(DateTimeConverter.class)
    private DateTime endTime;
    
    public GetObservationConstellation(){        
    }
    
    public GetObservationConstellation(String offering, Set<String> procedures,
            Set<String> observedProperties) {
        super();
        this.offering = offering;
        this.procedures = procedures;
        this.observedProperties = observedProperties;
    }

    public String getOffering() {
        return offering;
    }
    
    public void setOffering(String offering) {
        this.offering = offering;
    }
    
    public Set<String> getProcedures() {
        return procedures;
    }
    
    public void addProcedure(String procedure) {
        this.procedures.add(procedure);
    }
    
    public void addProcedures(Set<String> procedures) {
        this.procedures.addAll(procedures);
    }
    
    public Set<String> getObservedProperties() {
        return observedProperties;
    }

    public void addObservedProperty(String observedProperty) {
        this.observedProperties.add(observedProperty);
    }
    
    public void addObservedProperties(Set<String> observedProperties) {
        this.observedProperties.addAll(observedProperties);
    }
    
    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
        result = prime
                * result
                + ((observedProperties == null) ? 0 : observedProperties
                        .hashCode());
        result = prime * result
                + ((offering == null) ? 0 : offering.hashCode());
        result = prime * result
                + ((procedures == null) ? 0 : procedures.hashCode());
        result = prime * result
                + ((startTime == null) ? 0 : startTime.hashCode());
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
        GetObservationConstellation other = (GetObservationConstellation) obj;
        if (endTime == null) {
            if (other.endTime != null)
                return false;
        } else if (!endTime.equals(other.endTime))
            return false;
        if (observedProperties == null) {
            if (other.observedProperties != null)
                return false;
        } else if (!observedProperties.equals(other.observedProperties))
            return false;
        if (offering == null) {
            if (other.offering != null)
                return false;
        } else if (!offering.equals(other.offering))
            return false;
        if (procedures == null) {
            if (other.procedures != null)
                return false;
        } else if (!procedures.equals(other.procedures))
            return false;
        if (startTime == null) {
            if (other.startTime != null)
                return false;
        } else if (!startTime.equals(other.startTime))
            return false;
        return true;
    }
}