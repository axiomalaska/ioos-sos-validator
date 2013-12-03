package com.axiomalaska.ioos.sos.validator.test.m1_0;

import java.util.Arrays;
import java.util.List;

import net.opengis.gml.MetaDataPropertyDocument;
import net.opengis.gml.MetaDataPropertyType;
import net.opengis.gml.VersionDocument;
import net.opengis.ows.x11.CodeType;
import net.opengis.ows.x11.OperationsMetadataDocument.OperationsMetadata;
import net.opengis.ows.x11.ServiceIdentificationDocument.ServiceIdentification;
import net.opengis.sos.x10.CapabilitiesDocument;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNotNull;

import org.junit.Before;
import org.junit.Test;

import com.axiomalaska.ioos.sos.IoosSosConstants;
import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;
import com.axiomalaska.ioos.sos.validator.test.AbstractCapabilitiesTest;
import com.axiomalaska.ioos.sos.validator.util.XmlHelper;

public class CapabilitiesTestm1_0 extends AbstractCapabilitiesTest{
    public CapabilitiesTestm1_0(SosDocumentProvider provider) {
        super(provider);
    }

    private static CapabilitiesDocument xbCapabilities;

    @Before
    public void setUp() throws SosValidationException, CompositeSosValidationException{
        xbCapabilities = XmlHelper.castResult(provider, provider.getDocument(SosDocumentType.M1_0_CAPABILITIES),
                CapabilitiesDocument.class, SosDocumentType.M1_0_CAPABILITIES, CapabilitiesDocument.type);
    }

    @Override
    public SosDocumentType getSosDocumentType() {
        return SosDocumentType.M1_0_CAPABILITIES;
    }

    @Override
    protected XmlObject getSosDocument() {
        return xbCapabilities;
    }
    
    @Test
    public void checkMetaData() throws XmlException{
        assumeNotNull(xbCapabilities);
        
        //check ows:OperationsMetadata
        OperationsMetadata xbOperationsMetadata = xbCapabilities.getCapabilities().getOperationsMetadata();
        assertNotNull("OperationsMetadata is missing.", xbOperationsMetadata);
        
        //check ows:ExtendedCapabilities
        XmlObject xbExtendedCaps = xbOperationsMetadata.getExtendedCapabilities();
        assertNotNull("ExtendedCapabilities for " + IoosSosConstants.IOOS_TEMPLATE_VERSION + " is missing.", xbExtendedCaps);
        
        //check gml:metaDataProperty
        List<MetaDataPropertyDocument> metaDataProps = XmlHelper.getChildren(xbExtendedCaps, MetaDataPropertyDocument.class);
        assertFalse("no metaDataProperties found", metaDataProps.size() == 0);
        boolean ioosVersionFound = false;
        boolean softwareVersionFound = false;
        for (int i = 0; i < metaDataProps.size(); i++) {
            MetaDataPropertyType metaDataProperty = metaDataProps.get(i).getMetaDataProperty();
            assertNotNull("metaDataProperty title is missing", metaDataProperty.getTitle());
            assertTrue("metaDataProperty title is empty", metaDataProperty.getTitle().length() > 0);
            if (metaDataProperty.getTitle().equals(IoosSosConstants.IOOS_TEMPLATE_VERSION)) {
                ioosVersionFound = true;
                assertNotNull("metaDataProperty href is missing", metaDataProperty.getHref());
                assertTrue("metaDataProperty href is empty", metaDataProperty.getHref().length() > 0);
                assertEquals(IoosSosConstants.IOOS_TEMPLATE_VERSION + "'s href should be " + IoosSosConstants.IOOS_VERSION_DEFINITION,
                        IoosSosConstants.IOOS_VERSION_DEFINITION, metaDataProperty.getHref());            
                String version = checkVersion(metaDataProperty);
                assertEquals(IoosSosConstants.IOOS_VERSION_DEFINITION + " is not " + IoosSosConstants.IOOS_VERSION_M10,
                        IoosSosConstants.IOOS_VERSION_M10, version);                
            } else if (metaDataProperty.getTitle().equals(IoosSosConstants.SOFTWARE_VERSION)) {
                softwareVersionFound = true;
                checkVersion(metaDataProperty);
            }
        }

        if (!ioosVersionFound) {
            fail(IoosSosConstants.IOOS_VERSION_DEFINITION + "metaDataProperty not present");
        }

        if (!softwareVersionFound) {
            //don't fail, not required yet
//            fail(IoosSosConstants.SOFTWARE_VERSION + "metaDataProperty not present");
        }

    }

    private String checkVersion(MetaDataPropertyType metaDataProperty) throws XmlException {
        List<VersionDocument> versions = XmlHelper.getChildren(metaDataProperty, VersionDocument.class);
        assertFalse("Duplicate gml:version", versions.size() >= 2);
        assertFalse("gml:verison missing", versions.size() == 0);
        String version = versions.get(0).getVersion();
        return version;
    }
    
    @Test
    public void checkServiceIdentification() throws XmlException{
        assumeNotNull(xbCapabilities);        

        //check ows:ServiceIdentification
        ServiceIdentification xbServiceIdentification = xbCapabilities.getCapabilities().getServiceIdentification();
        assertNotNull("ows:ServiceIdentification is missing.", xbServiceIdentification);

        //abstract
        assertTrue("ows:ServiceIdentification/ows:Abstract is missing.", xbServiceIdentification.getAbstractArray() != null
                && xbServiceIdentification.getAbstractArray().length > 0);
        assertFalse("Multiple ows:ServiceIdentification/ows:Abstracts.", xbServiceIdentification.getAbstractArray().length > 1);

        //keywords
//        assertTrue("ows:ServiceIdentification/ows:Keywords is missing.", xbServiceIdentification.getKeywordsArray() != null
//                && xbServiceIdentification.getKeywordsArray().length > 0);
//        assertFalse("Multiple ows:ServiceIdentification/ows:Keywords.", xbServiceIdentification.getKeywordsArray().length > 1);
//        assertTrue("ows:ServiceIdentification/ows:Keywords is empty.", xbServiceIdentification.getKeywordsArray()[0].getKeywordArray() != null
//                && xbServiceIdentification.getKeywordsArray()[0].getKeywordArray().length > 0);        
        
        //serviceType
        assertNotNull("ows:ServiceIdentification/ows:ServiceType is missing.", xbServiceIdentification.getServiceType());
        CodeType xbServiceType = xbServiceIdentification.getServiceType();
        assertEquals("ows:ServiceIdentification/ows:ServiceType value is incorrect", IoosSosConstants.SI_SERVICE_TYPE,
                xbServiceType.getStringValue());
        assertEquals("ows:ServiceIdentification/ows:ServiceType codeSpace is incorrect", IoosSosConstants.SI_SERVICE_TYPE_CODE_SPACE,
                xbServiceType.getCodeSpace());
        
        //serviceTypeVersion
        assertTrue("ows:ServiceIdentification/ows:ServiceTypeVersion is missing.",
                xbServiceIdentification.getServiceTypeVersionArray() != null
                && xbServiceIdentification.getServiceTypeVersionArray().length > 0);        
        assertTrue("ows:ServiceIdentification/ows:ServiceTypeVersion doesn't contain " + IoosSosConstants.SOS_V1,
                Arrays.asList(xbServiceIdentification.getServiceTypeVersionArray()).contains(IoosSosConstants.SOS_V1));
        
        //fees
        assertNotNull("ows:ServiceIdentification/ows:Fees is missing.", xbServiceIdentification.getFees());        
        assertEquals("ows:ServiceIdentification/ows:Fees is invalid.", IoosSosConstants.NONE,
                xbServiceIdentification.getFees());

        //access constraints
        assertTrue("ows:ServiceIdentification/ows:AccessConstraints is missing.",
                xbServiceIdentification.getAccessConstraintsArray() != null
                && xbServiceIdentification.getAccessConstraintsArray().length > 0);        
        assertTrue("ows:ServiceIdentification/ows:AccessConstraints doesn't contain " + IoosSosConstants.NONE,
                Arrays.asList(xbServiceIdentification.getAccessConstraintsArray()).contains(IoosSosConstants.NONE));
    }
}