package com.axiomalaska.ioos.sos.validator.provider;

import java.net.MalformedURLException;
import java.net.URL;

import com.axiomalaska.ioos.sos.validator.exception.InvalidRequestConfigurationException;
import com.axiomalaska.ioos.sos.validator.exception.InvalidUrlException;
import com.axiomalaska.ioos.sos.validator.provider.http.KvpHttpSosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.http.config.RequestConfiguration;

public class TestKvpHttpSosDocumentProvider extends KvpHttpSosDocumentProvider{
    public TestKvpHttpSosDocumentProvider() throws InvalidUrlException, MalformedURLException,
            InvalidRequestConfigurationException {
        super(new URL("http://localhost:9090/sos/kvp"),
                RequestConfiguration.exampleConfig());
    }    
}
