package com.axiomalaska.ioos.sos.validator.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

import org.apache.log4j.Logger;

public class SosDocumentProviderRepository {
    private static SosDocumentProviderRepository instance;
    private List<SosDocumentProvider> sosDocumentProviders = new ArrayList<SosDocumentProvider>();
    private static final Logger LOG = Logger.getLogger(SosDocumentProviderRepository.class);
    
    public static List<SosDocumentProvider> providers(){
        return instance().getSosDocumentProviders();
    }

    private static SosDocumentProviderRepository instance(){
        if (instance == null){
            instance = new SosDocumentProviderRepository();
        }
        return instance;
    }

    public static void addProvider(SosDocumentProvider provider){
        instance().sosDocumentProviders.add(provider);
    }

    public static void addProviders(List<SosDocumentProvider> providers){
        instance().sosDocumentProviders.addAll(providers);
    }
        
    private SosDocumentProviderRepository(List<SosDocumentProvider> providers){
        sosDocumentProviders = new ArrayList<SosDocumentProvider>(providers);
    }
    
    private SosDocumentProviderRepository(){
        ServiceLoader<SosDocumentProvider> providerLoader = ServiceLoader.load(SosDocumentProvider.class); 
        for (SosDocumentProvider loadedProvider : providerLoader){
            sosDocumentProviders.add(loadedProvider);
        }

        if (!sosDocumentProviders.isEmpty()){
            LOG.info("Loaded SosDocumentProviders: " + sosDocumentProviders.toString());
        }
    }
    
    private List<SosDocumentProvider> getSosDocumentProviders(){
        if (sosDocumentProviders.isEmpty()){
            throw new RuntimeException("No SosDocumentProviders detected!");
        }
        return Collections.unmodifiableList(sosDocumentProviders);
    }
}
