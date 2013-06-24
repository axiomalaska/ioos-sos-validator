package com.axiomalaska.ioos.sos.validator.test.m1_0;

import java.util.List;

import net.opengis.gml.MetaDataPropertyDocument;
import net.opengis.gml.MetaDataPropertyType;
import net.opengis.gml.VersionDocument;
import net.opengis.ows.x11.OperationsMetadataDocument.OperationsMetadata;
import net.opengis.sos.x10.CapabilitiesDocument;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.axiomalaska.ioos.sos.IoosSosConstants;
import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.InvalidSosDocumentException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;
import com.axiomalaska.ioos.sos.validator.test.AbstractGetCapabilitiesTest;
import com.axiomalaska.ioos.sos.validator.util.XmlHelper;

public class CapabilitiesTestm1_0 extends AbstractGetCapabilitiesTest{
    public CapabilitiesTestm1_0(SosDocumentProvider provider) {
        super(provider);
    }

    private static CapabilitiesDocument xbCapabilities;

    @Before
    public void setUp() throws SosValidationException, CompositeSosValidationException{
        XmlObject xmlObject = provider.getDocument(SosDocumentType.M1_0_CAPABILITIES);
        if (!(xmlObject instanceof CapabilitiesDocument)){
            throw new InvalidSosDocumentException(provider, SosDocumentType.M1_0_CAPABILITIES,
                    CapabilitiesDocument.type);
        }
        xbCapabilities = (CapabilitiesDocument) xmlObject;
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
    public void checkIoosVersion() throws XmlException{
        Assume.assumeNotNull(xbCapabilities);
        
        //check ows:OperationsMetadata
        OperationsMetadata xbOperationsMetadata = xbCapabilities.getCapabilities().getOperationsMetadata();
        Assert.assertNotNull("OperationsMetadata is missing.", xbOperationsMetadata);
        
        //check ows:ExtendedCapabilities
        XmlObject xbExtendedCaps = xbOperationsMetadata.getExtendedCapabilities();
        Assert.assertNotNull("ExtendedCapabilities for " + IoosSosConstants.IOOS_TEMPLATE_VERSION + " is missing.", xbExtendedCaps);
        
        //check gml:metaDataProperty
        List<MetaDataPropertyDocument> metaDataProps = XmlHelper.getChildren(xbExtendedCaps, MetaDataPropertyDocument.class);
        Assert.assertFalse("Duplicate " + IoosSosConstants.IOOS_TEMPLATE_VERSION, metaDataProps.size() >= 2);
        Assert.assertFalse(IoosSosConstants.IOOS_TEMPLATE_VERSION + " missing", metaDataProps.size() == 0);
        MetaDataPropertyType xbTemplateVersionMetadata = metaDataProps.get(0).getMetaDataProperty();
        Assert.assertNotNull(IoosSosConstants.IOOS_TEMPLATE_VERSION + " missing href", xbTemplateVersionMetadata.getHref());
        Assert.assertTrue(IoosSosConstants.IOOS_TEMPLATE_VERSION + " href is empty", xbTemplateVersionMetadata.getHref().length() > 0);
        Assert.assertEquals(IoosSosConstants.IOOS_TEMPLATE_VERSION + "'s href is not " + IoosSosConstants.IOOS_VERSION_DEFINITION,
                IoosSosConstants.IOOS_VERSION_DEFINITION, xbTemplateVersionMetadata.getHref());
                
        //check gml:version
        List<VersionDocument> versions = XmlHelper.getChildren(xbTemplateVersionMetadata, VersionDocument.class);
        Assert.assertFalse("Duplicate gml:version", versions.size() >= 2);
        Assert.assertFalse("gml:verison missing", versions.size() == 0);
        String version = versions.get(0).getVersion();
        Assert.assertEquals("gml:version is not " + IoosSosConstants.IOOS_VERSION_M10,
                IoosSosConstants.IOOS_VERSION_M10, version);
    }
}