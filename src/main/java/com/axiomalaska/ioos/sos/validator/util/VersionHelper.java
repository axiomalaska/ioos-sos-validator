package com.axiomalaska.ioos.sos.validator.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VersionHelper {
    public static String getVersion(){
        String path = "/META-INF/maven/com.axiomalaska/ioos-sos-validator/pom.properties";
        InputStream stream = VersionHelper.class.getResourceAsStream(path);
        Properties props = new Properties();
        try {
            props.load(stream);
        } catch (IOException e) {
            //silent fail, not critical
        }
        Object version = props.get("version"); 
        if (version != null && version instanceof String) {
            return (String) version;            
        }
        return "UNKNOWN";        
    }
}
