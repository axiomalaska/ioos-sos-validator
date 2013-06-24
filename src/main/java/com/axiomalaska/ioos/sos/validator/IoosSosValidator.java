package com.axiomalaska.ioos.sos.validator;

import java.io.File;
import java.net.URL;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.axiomalaska.ioos.sos.validator.provider.DirectorySosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProviderRepository;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;
import com.axiomalaska.ioos.sos.validator.provider.http.IoosGoogleCodeProviderm1_0;
import com.axiomalaska.ioos.sos.validator.provider.http.KvpHttpSosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.http.PoxHttpSosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.test.AllTests;
import com.axiomalaska.ioos.sos.validator.util.VersionHelper;

public class IoosSosValidator {
    public static final String HELP = "help";
    public static final String URL = "url";
    public static final String KVP_URL = "kvpUrl";
    public static final String POX_URL = "poxUrl";
    public static final String DIR = "dir";
    public static final String GOOGLE_CODE = "google-code";
    
    public static void main(String[] args){
        Options options = new Options();
        
        OptionGroup urlGroup = new OptionGroup();
        Option help = new Option("?",HELP,true,"Display help message.");
        Option url = new Option("u",URL,true,"Endpoint for all requests to SOS server. Overridden by kvpUrl and poxUrl.");
        Option kvpUrl = new Option("k",KVP_URL,true,"Endpoint for HTTP KVP (GET) requests to SOS server. Defaults to url.");
        Option poxUrl = new Option("p",POX_URL,true,"Endpoint for HTTP POX (plain-old-XML POST) requests to SOS server. Defaults to url.");
        urlGroup.addOption(url);
        urlGroup.addOption(kvpUrl);
        urlGroup.addOption(poxUrl);
        
        Option dir = new Option("d",DIR,true,"Path to local directory containing xml files with standard names");
        Option googleCode = new Option("gc",GOOGLE_CODE,true,"Validate against Google Code IOOS SOS template respository (specify version, i.e. 1.0)");
        
        options.addOption(help);
        options.addOption(dir);
        options.addOption(googleCode);
        options.addOptionGroup(urlGroup);        
        
        CommandLine line = null;
        try {
            line = new BasicParser().parse( options, args );            
        } catch (ParseException e) {
            System.out.println( "Unexpected exception: " + e.getMessage() );
            System.exit(1);
        }

        if (line.hasOption(HELP)) {
            displayHelp(options);
            System.exit(0);
        } else if (line.hasOption(GOOGLE_CODE)){
            String version = line.getOptionValue(GOOGLE_CODE);
            if (version.equals("1.0")){
                try {
                    SosDocumentProviderRepository.addProvider(new IoosGoogleCodeProviderm1_0());
                } catch (Exception e){
                    System.out.println("Google code URL is invalid, contact developer.");
                    System.exit(1);
                }
            } else {
                System.out.println("Invalid milestone version. Valid versions are: 1.0.");
                System.exit(1);                
            }
        } else if (line.hasOption(DIR)){
            String dirStr = line.getOptionValue(DIR);
            File rootDir = new File(dirStr);
            if (!rootDir.exists()) {
                //try a relative path
                rootDir = new File("./" + dirStr);
            }
            
            if (!rootDir.exists()) {
                System.out.println("Local directory " + dirStr + " doesn't exist");
                System.out.println("Current directory is " + System.getProperty("user.dir"));
                System.exit(1);
            }
            SosDocumentProviderRepository.addProvider(new DirectorySosDocumentProvider(rootDir));
        } else if (line.hasOption(URL) || (line.hasOption(KVP_URL) || line.hasOption(POX_URL))){
            String kvpUrlValue = line.hasOption(KVP_URL) ? line.getOptionValue(KVP_URL) : line.getOptionValue(URL);
            String poxUrlValue = line.hasOption(POX_URL) ? line.getOptionValue(POX_URL) : line.getOptionValue(URL);
            
            if (kvpUrlValue != null) {
                try {
                    SosDocumentProviderRepository.addProvider(new KvpHttpSosDocumentProvider(new URL(kvpUrlValue)));
                } catch (Exception e) {
                    System.out.println("Invalid kvp url:" + e.getMessage() );
                    System.exit(1);
                }
            }

            if (poxUrlValue != null) {
                try {
                    SosDocumentProviderRepository.addProvider(new PoxHttpSosDocumentProvider(new URL(poxUrlValue)));
                } catch (Exception e) {
                    System.out.println("Invalid pox url:" + e.getMessage() );
                    System.exit(1);
                }
            }
        } else {
            displayHelp(options);
            System.exit(1); 
        }

        if (SosDocumentProviderRepository.providers().isEmpty()){
            System.out.println("No SosDocumentProviders! Exiting.");
            System.exit(1);
        } else {
            System.out.println("SosDocumentProviders:");
            for (SosDocumentProvider provider : SosDocumentProviderRepository.providers()){
                System.out.println(provider);
            }
        }
        
        System.out.println();
        System.out.println("Running tests...");

        Result result = JUnitCore.runClasses(AllTests.class);
        for (Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }

        System.out.println();
        System.out.println("Tests complete");

        System.out.println();
        System.out.println("Tests performed: " + result.getRunCount());
        System.out.println("Tests ignored: " + result.getIgnoreCount());
        System.out.println("Tests failed: " + result.getFailureCount());
        System.out.println("Run time: " + result.getRunTime());        

        System.out.println();
        if (result.wasSuccessful()){
            System.out.println("VALIDATION SUCCESSFUL!");
        } else {
            System.out.println("VALIDATION FAILED");
            System.exit(1);
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
