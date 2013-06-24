package com.axiomalaska.ioos.sos.validator.provider;

import java.net.MalformedURLException;
import java.net.URL;

import com.axiomalaska.ioos.sos.validator.config.InvalidUrlException;
import com.axiomalaska.ioos.sos.validator.provider.http.KvpHttpSosDocumentProvider;

public class TestKvpHttpSosDocumentProvider extends KvpHttpSosDocumentProvider{
    public TestKvpHttpSosDocumentProvider() throws InvalidUrlException, MalformedURLException {
        super(new URL("http://ioososs.axiomalaska.com/52n-sos-ioos/sos/kvp"));
    }    
}
