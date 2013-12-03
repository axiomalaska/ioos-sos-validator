package com.axiomalaska.ioos.sos.validator;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axiomalaska.ioos.sos.validator.exception.CompositeSosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException;
import com.axiomalaska.ioos.sos.validator.exception.SosValidationException.Severity;
import com.axiomalaska.ioos.sos.validator.provider.DirectorySosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProviderRepository;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;
import com.axiomalaska.ioos.sos.validator.provider.http.IoosGoogleCodeProvider;
import com.axiomalaska.ioos.sos.validator.provider.http.KvpHttpSosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.http.PoxHttpSosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.http.config.RequestConfiguration;
import com.axiomalaska.ioos.sos.validator.test.AllTests;
import com.axiomalaska.ioos.sos.validator.util.VersionHelper;
import com.axiomalaska.ioos.sos.validator.xstream.XStreamRepository;

public class IoosSosValidator {
    public static final Logger LOG = LoggerFactory.getLogger(IoosSosValidator.class);
    private static final String LOG_DIVIDER = StringUtils.repeat('-',  70);
    
    public static final String HELP = "help";
    public static final String URL = "url";
    public static final String KVP_URL = "kvp-url";
    public static final String POX_URL = "pox-url";
    public static final String REQUEST_CONFIG = "request-config";
    public static final String EXAMPLE_REQUEST_CONFIG = "example-request-config";
    public static final String DIR = "dir";
    public static final String GOOGLE_CODE = "google-code";
    
    public static void main(String[] args){
        Options options = new Options();
        
        Option help = new Option("?",HELP,false,"Display help message.");
        Option url = new Option("u",URL,true,"Endpoint for all requests to SOS server. Overridden by kvpUrl and poxUrl.");
        Option kvpUrl = new Option("k",KVP_URL,true,"Endpoint for HTTP KVP (GET) requests to SOS server. Defaults to url.");
        Option poxUrl = new Option("p",POX_URL,true,"Endpoint for HTTP POX (plain-old-XML POST) requests to SOS server. Defaults to url.");
        Option requestConfig = new Option("rc",REQUEST_CONFIG,true,"Path to xml configuration for SOS requests.");        
        Option exampleRequestConfig = new Option("erc",EXAMPLE_REQUEST_CONFIG,false,"Print an example request configuration.");
        
        Option dir = new Option("d",DIR,true,"Path to local directory containing xml files with standard names");
        Option googleCode = new Option("gc",GOOGLE_CODE,true,"Validate against Google Code IOOS SOS template respository (specify version, i.e. 1.0)");
        
        options.addOption(help);
        options.addOption(dir);
        options.addOption(googleCode);
        options.addOption(url);
        options.addOption(kvpUrl);
        options.addOption(poxUrl);
        options.addOption(requestConfig);
        options.addOption(exampleRequestConfig);
        
        CommandLine line = null;
        try {
            line = new BasicParser().parse( options, args );            
        } catch (ParseException e) {
            LOG.error("Unexpected exception: " + e.getMessage());
            System.exit(1);
        }

        if (line.hasOption(HELP)) {
            displayHelp(options);
            System.exit(0);
        } else if (line.hasOption(EXAMPLE_REQUEST_CONFIG)) {
            LOG.info(XStreamRepository.instance().toXML(RequestConfiguration.exampleConfig()));
            System.exit(0);            
        } else if (line.hasOption(GOOGLE_CODE)){
            String version = line.getOptionValue(GOOGLE_CODE);
            if (version.equals("1.0")){
                try {
                    SosDocumentProviderRepository.addProvider(new IoosGoogleCodeProvider());
                } catch (Exception e){
                    LOG.error("Google code URL is invalid, contact developer.");
                    System.exit(1);
                }
            } else {
                LOG.error("Invalid milestone version. Valid versions are: 1.0.");
                System.exit(1);                
            }
        } else if (line.hasOption(DIR)){
            String dirStr = line.getOptionValue(DIR);
            File rootDir = getFile(dirStr);
            if (!rootDir.exists()) {
                LOG.error("Local directory " + dirStr + " doesn't exist");
                LOG.info("Current directory is " + System.getProperty("user.dir"));
                System.exit(1);
            }
            SosDocumentProviderRepository.addProvider(new DirectorySosDocumentProvider(rootDir));
        } else if (line.hasOption(URL) || (line.hasOption(KVP_URL) || line.hasOption(POX_URL))){
            String kvpUrlValue = line.hasOption(KVP_URL) ? line.getOptionValue(KVP_URL) : line.getOptionValue(URL);
            String poxUrlValue = line.hasOption(POX_URL) ? line.getOptionValue(POX_URL) : line.getOptionValue(URL);
            
            if (!line.hasOption(REQUEST_CONFIG)) {
                LOG.error("-" + requestConfig.getOpt() + " (--" + requestConfig.getLongOpt() + ") is required"
                        + " when testing SOS servers");
                System.exit(1);                
            }
            
            String requestConfigValue = line.getOptionValue(REQUEST_CONFIG);
            File requestConfigFile = getFile(requestConfigValue);
            if (!requestConfigFile.exists()){
                LOG.error("-" + requestConfig.getOpt() + " (--" + requestConfig.getLongOpt() + ") file " +
                        requestConfigValue + " doesn't exist");
                System.exit(1);                                
            }
            Object requestConfigObject = XStreamRepository.instance().fromXML(requestConfigFile);
            if (!(requestConfigObject instanceof RequestConfiguration)) {
                LOG.error("-" + requestConfig.getOpt() + " (--" + requestConfig.getLongOpt() + ") file " +
                        requestConfigValue + " is not a valid RequestConfiguration");
                System.exit(1);                                                
            }
            RequestConfiguration requestConfiguration = (RequestConfiguration) requestConfigObject;

            if (kvpUrlValue != null) {
                try {
                    SosDocumentProviderRepository.addProvider(new KvpHttpSosDocumentProvider(
                            new URL(kvpUrlValue), requestConfiguration));
                } catch (Exception e) {
                    LOG.error("Invalid kvp url:" + e.getMessage() );
                    System.exit(1);
                }
            }

            if (poxUrlValue != null) {
                try {
                    SosDocumentProviderRepository.addProvider(new PoxHttpSosDocumentProvider(
                            new URL(poxUrlValue), requestConfiguration));
                } catch (Exception e) {
                    LOG.error("Invalid pox url:" + e.getMessage() );
                    System.exit(1);
                }
            }
        } else {
            displayHelp(options);
            System.exit(1); 
        }

        if (SosDocumentProviderRepository.providers().isEmpty()){
            LOG.error("No SosDocumentProviders! Exiting.");
            System.exit(1);
        } else {
            StringBuilder providers = new StringBuilder();
            int providerCounter = 0;
            for (SosDocumentProvider provider : SosDocumentProviderRepository.providers()){
                if(providerCounter++ > 0) {
                    providers.append(", ");
                }
                providers.append(provider.toString());
            }            
            LOG.info("SosDocumentProviders: " + providers.toString());
        }
        
        LOG.info("Running tests...");

        Result result = JUnitCore.runClasses(AllTests.class);
        if (result.getFailureCount() > 0) {
            LOG.warn("TEST FAILURES");
            for (Failure failure : result.getFailures()){
                LOG.info(LOG_DIVIDER);
                LOG.warn(failure.getDescription().getClassName());
                LOG.warn(failure.getDescription().getMethodName());
                LOG.warn(failure.getMessage());
            }
            LOG.info(LOG_DIVIDER);            
        }
        LOG.info("Tests complete");

        LOG.info(LOG_DIVIDER);
        LOG.info("Tests performed: " + result.getRunCount());
        LOG.info("Tests ignored: " + result.getIgnoreCount());
        LOG.info("Tests failed: " + result.getFailureCount());
        LOG.info("Run time: " + result.getRunTime());        
        LOG.info(LOG_DIVIDER);
        
        List<Throwable> exceptions = new ArrayList<Throwable>();
        for (Failure failure : result.getFailures()){
            exceptions.add(failure.getException());
        }
        
        Map<Severity,Integer> severityCounts = processExceptions(exceptions);
        LOG.info("Failure severity summary:");
        for (Severity severity : Severity.values()) {
            LOG.info(severity.name() + ": " + severityCounts.get(severity));
        }
        
        LOG.info(LOG_DIVIDER);
        if (severityCounts.get(Severity.FATAL).equals(0)){
            LOG.info("VALIDATION SUCCESSFUL");
        } else {
            LOG.warn("VALIDATION FAILED");
            System.exit(1);
        }
    }
    
    private static File getFile(String path) {
        File file = new File(path);
        //if absolute doesn't exist, try a relative path
        if (!file.exists()) {
            file = new File("./" + path);
        }
        return file;
    }
    
    private static Map<Severity,Integer> processExceptions(List<Throwable> exceptions) {
        HashMap<Severity, Integer> map = new HashMap<Severity,Integer>();
        for (Severity severity : Severity.values()) {
            map.put(severity, 0);
        }
        processExceptions(exceptions, map);
        return map;
    }

    private static void processExceptions(List<Throwable> exceptions,
            Map<Severity,Integer> map) {
        for (Throwable exception : exceptions) {
            processException(exception, map);
        }
    }

    private static void processException(Throwable exception, Map<Severity,Integer> map) {
        if (exception instanceof SosValidationException) {
            Severity severity = ((SosValidationException) exception).getSeverity();
            map.put(severity, map.get(severity) + 1);
        } else if (exception instanceof CompositeSosValidationException) {
            CompositeSosValidationException compositeException = (CompositeSosValidationException) exception;
            for (SosValidationException sve : compositeException.getExceptions()) {
                processException(sve, map);
            }
        } else {
            //if other type of exception, assume fatal
            map.put(Severity.FATAL, map.get(Severity.FATAL) + 1);
        }
    }
        
    private static void displayHelp(Options options){
        HelpFormatter formatter = new HelpFormatter();
        String usage = "java -jar ioos-sos-validator.jar [OPTIONS]";
        
        String header = "\nIOOS SOS Validator, version " + VersionHelper.getVersion()
        		+ "\nValidate SOS response documents to IOOS standards. Can validate against a live SOS server, a local"
                + " directory of XML files, or the IOOS Google Code SOS response respository. Currently supports IOOS SOS milestone 1.0.";
        
        StringBuilder footer = new StringBuilder("\nLocal directory filename patterns:");
        for (SosDocumentType docType : SosDocumentType.values()) {
            footer.append("\n" + DirectorySosDocumentProvider.getDocumentFilename(docType));
        }

        formatter.printHelp(usage, header, options, footer.toString());        
    }
}
