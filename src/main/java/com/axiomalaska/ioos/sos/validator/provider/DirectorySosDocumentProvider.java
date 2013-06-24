package com.axiomalaska.ioos.sos.validator.provider;

import java.io.File;
import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;

public class DirectorySosDocumentProvider extends SosDocumentProvider{
    private File rootDir;
    
    public DirectorySosDocumentProvider(File rootDir) {
        super();
        this.rootDir = rootDir;
    }

    public static String getDocumentFilename(SosDocumentType document) {
        return document.name() + ".xml";
    }
    
    @Override
    protected XmlObject getDocumentXml(SosDocumentType document) throws SosValidationException{
        File file = new File(rootDir, getDocumentFilename(document));
        if (file.exists()){
            try {
                return XmlObject.Factory.parse(file);
            } catch (XmlException e) {
                throw new SosValidationException(e);
            } catch (IOException e) {
                throw new SosValidationException(e);
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return super.toString() + " (" + rootDir.getAbsolutePath() + ")";
    }
}
