package com.axiomalaska.ioos.sos.validator.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.validator.routines.RegexValidator;
import org.apache.commons.validator.routines.UrlValidator;

public class SosValidationConfig {
    private URL kvpUrl;
    private URL poxUrl;
    private final UrlValidator urlValidator = new UrlValidator(new RegexValidator(".*"),0);
    
    public SosValidationConfig() {
        super();
    }
    
    public SosValidationConfig(URL kvpUrl, URL poxUrl) throws InvalidUrlException {
        super();
        checkUrl(kvpUrl);
        checkUrl(poxUrl);
        this.kvpUrl = kvpUrl;
        this.poxUrl = poxUrl;
    }

    public URL getKvpUrl() {
        return kvpUrl;
    }

    public URL getPoxUrl() {
        return poxUrl;
    }
    
    private void checkUrl(URL url) throws InvalidUrlException{
        if (!urlValidator.isValid(url.toExternalForm())){
            throw new InvalidUrlException(url.toExternalForm() + " is not a valid URL");
        }
    }
    
    
    public static SosValidationConfig read(Configuration config) throws ConfigurationException{
        URL url = createUrl(config, SosValidationConfigConstants.URL);
        
        URL kvpUrl = createUrl(config, SosValidationConfigConstants.KVP_URL, url);
        if (kvpUrl == null){
            throw new ConfigurationException("Must set " + SosValidationConfigConstants.URL
                    + " and/or " + SosValidationConfigConstants.KVP_URL);
        }
        
        URL poxUrl = createUrl(config, SosValidationConfigConstants.POX_URL, url);
        if (poxUrl == null){
            throw new ConfigurationException("Must set " + SosValidationConfigConstants.URL
                    + " and/or " + SosValidationConfigConstants.POX_URL);
        }
 
        SosValidationConfig svc;
        try {
            svc = new SosValidationConfig(kvpUrl, poxUrl);
        } catch (InvalidUrlException e) {
            throw new ConfigurationException(e);
        }
        return svc;
    }
    
    private static URL createUrl(Configuration config, String property) throws ConfigurationException{
        return createUrl(config, property, null);
    }
    
    private static URL createUrl(Configuration config, String property, URL defaultValue) throws ConfigurationException{
        String url = config.getString(property);
        if (url != null) {
            try {
                return new URL(url);
            } catch (MalformedURLException e) {
                throw new ConfigurationException( new InvalidUrlException(url,e));
            }
        }
        return defaultValue;        
    }
}
