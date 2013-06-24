package com.axiomalaska.ioos.sos.validator.provider;

import java.io.File;

public class TestDirectorySosDocumentProvider extends DirectorySosDocumentProvider{

    public TestDirectorySosDocumentProvider() {
        super(new File("src/test/resources/documents"));
    }    
}
