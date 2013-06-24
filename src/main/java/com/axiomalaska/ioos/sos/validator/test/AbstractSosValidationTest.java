package com.axiomalaska.ioos.sos.validator.test;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.axiomalaska.ioos.sos.validator.exception.SchemaValidationException;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProviderRepository;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;

@RunWith(value = Parameterized.class)
public abstract class AbstractSosValidationTest {
    protected SosDocumentProvider provider;
    
    public abstract SosDocumentType getSosDocumentType();
    protected abstract XmlObject getSosDocument();
            
    @Rule
    public ErrorCollector collector = new ErrorCollector();

    public AbstractSosValidationTest(SosDocumentProvider provider) {
        super();
        this.provider = provider;
    }

    @Parameters(name="{0}")
    public static Collection<Object[]> getProviders(){
        Collection<Object[]> testParams = new ArrayList<Object[]>();
        for (SosDocumentProvider provider : SosDocumentProviderRepository.providers()) {
            testParams.add( new Object[]{ provider });
        }        
        return testParams;
    }

    @Test
    public void testSchemaValidity(){        
        ArrayList<XmlValidationError> validationErrors = new ArrayList<XmlValidationError>();
        XmlOptions validationOptions = new XmlOptions();
        validationOptions.setErrorListener(validationErrors);
        
        boolean valid = getSosDocument().validate(validationOptions);
        if (!valid){
            for (XmlValidationError validationError : validationErrors) {
                collector.addError(new SchemaValidationException(validationError));
            }
        }
    }
}
