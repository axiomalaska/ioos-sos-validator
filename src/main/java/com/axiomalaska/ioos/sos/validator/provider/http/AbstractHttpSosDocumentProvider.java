package com.axiomalaska.ioos.sos.validator.provider.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.List;

import net.opengis.ows.x11.ExceptionReportDocument;
import net.opengis.ows.x11.ExceptionType;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.xmlbeans.XmlObject;

import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.InvalidUrlException;
import com.axiomalaska.ioos.sos.validator.exception.SosHttpRequestException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProvider;

public abstract class AbstractHttpSosDocumentProvider extends SosDocumentProvider {
    ClientConnectionManager cm = new PoolingClientConnectionManager();    
    private HttpClient client = new DefaultHttpClient(cm);
    protected URL url;    
    
    public AbstractHttpSosDocumentProvider(URL url) throws InvalidUrlException {
        super();
        this.url = url;
    }

    public String getUrl(List<NameValuePair> kvps) {        
        return url.toExternalForm() + "?" + URLEncodedUtils.format(kvps, "UTF-8");
    }
    
    
    protected XmlObject sendRequest(HttpUriRequest request) throws SosValidationException, CompositeSosValidationException{
        HttpResponse response;
        try {
            response = client.execute(request);
        } catch (Exception e) {
            throw new SosValidationException(e);
        }
        assertEquals("Non-OK http status code", HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertNotNull("Response missing entity", response.getEntity() );
        try {
            assertNotNull("Response missing entity content", response.getEntity().getContent() );
        } catch (Exception e) {
            throw new SosValidationException(e);
        }
 
        XmlObject xbXml;
        try {
            xbXml = XmlObject.Factory.parse( response.getEntity().getContent() );
        } catch (Exception e) {
            throw new SosValidationException(e);
        }
        assertNotNull("XML response could not be parsed", xbXml);

        if( xbXml instanceof ExceptionReportDocument ){
            ExceptionReportDocument xbExceptionReportDoc = (ExceptionReportDocument) xbXml;
            if (xbExceptionReportDoc.getExceptionReport() == null
                    || xbExceptionReportDoc.getExceptionReport().getExceptionArray() == null
                    || xbExceptionReportDoc.getExceptionReport().getExceptionArray().length == 0){
                fail("Request failed with empty exception report");
            }
            CompositeSosValidationException compositeException = new CompositeSosValidationException();
            for (int i = 0, len = xbExceptionReportDoc.getExceptionReport().getExceptionArray().length; i < len; i++){
                ExceptionType xbException = xbExceptionReportDoc.getExceptionReport().getExceptionArray()[i];
                compositeException.addException(new SosHttpRequestException(xbException.toString()));
            }
            if (!compositeException.isEmpty()) {
                throw compositeException;
            }
        }

        return xbXml;             
    }

    @Override
    public String toString() {
        return super.toString() + " (" + url.toExternalForm() + ")";
    }    
}
