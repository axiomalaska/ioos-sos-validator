package com.axiomalaska.ioos.sos.validator.provider;

import java.net.MalformedURLException;
import java.net.URL;

import com.axiomalaska.ioos.sos.validator.exception.InvalidRequestConfigurationException;
import com.axiomalaska.ioos.sos.validator.exception.InvalidUrlException;
import com.axiomalaska.ioos.sos.validator.provider.http.PoxHttpSosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.http.config.RequestConfiguration;

public class TestPoxHttpSosDocumentProvider extends PoxHttpSosDocumentProvider{
    public TestPoxHttpSosDocumentProvider() throws InvalidUrlException, MalformedURLException,
            InvalidRequestConfigurationException {
        super(new URL("http://localhost:9090/webapp-ioos/sos/pox"),
                RequestConfiguration.exampleConfig());
    }    
}
