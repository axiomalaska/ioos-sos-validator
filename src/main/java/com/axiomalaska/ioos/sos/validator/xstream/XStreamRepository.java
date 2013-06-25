package com.axiomalaska.ioos.sos.validator.xstream;

import com.axiomalaska.ioos.sos.validator.provider.http.config.GetObservationConstellation;
import com.axiomalaska.ioos.sos.validator.provider.http.config.RequestConfiguration;
import com.thoughtworks.xstream.XStream;

public class XStreamRepository {
    private static XStream instance;
    
    private static XStream initialize() {
        instance = new XStream();
        instance.processAnnotations(RequestConfiguration.class);
        instance.processAnnotations(GetObservationConstellation.class);
        return instance;
    }
    
    public static final XStream instance(){
        if (instance == null){
            instance = initialize();
        }
        return instance;
    }
    
    public static void main(String[] args) {
        System.out.println(XStreamRepository.instance().toXML(RequestConfiguration.exampleConfig()));
    }
}
