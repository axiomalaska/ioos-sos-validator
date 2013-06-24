package com.axiomalaska.ioos.sos.validator.provider.http;

import java.util.Set;

public class GetObservationConstellation {
    private String offering;
    private Set<String> procedures;
    private Set<String> observedProperties;

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
    public void setProcedures(Set<String> procedures) {
        this.procedures = procedures;
    }
    public Set<String> getObservedProperties() {
        return observedProperties;
    }
    public void setObservedProperties(Set<String> observedProperties) {
        this.observedProperties = observedProperties;
    }
}