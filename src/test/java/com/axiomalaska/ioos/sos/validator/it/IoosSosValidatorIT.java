package com.axiomalaska.ioos.sos.validator.it;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.axiomalaska.ioos.sos.validator.IoosSosValidator;
import static org.junit.Assume.assumeNotNull;

public class IoosSosValidatorIT {
    private static final File shadedJar = getShadedJar("ioos-sos-validator");

    protected static File getShadedJar( String prefix ){
        for( File file : new File("target").listFiles() ){
            if( file.getName().matches( prefix + "-.*?-shaded.jar") ){
                return file;
            }
        }
        return null;
    }

    public int exec( File jar, String[] args ) throws IOException, InterruptedException{
        List<String> argList = new ArrayList<String>( Arrays.asList( args ) );
        argList.add(0,"java");
        argList.add(1,"-jar");
        argList.add(2,jar.getCanonicalPath());
        ProcessBuilder pb = new ProcessBuilder( argList );
        pb.redirectOutput( Redirect.INHERIT );
        pb.redirectError( Redirect.INHERIT );
        Process p = pb.start();
        p.waitFor();
        return p.exitValue();
    }
  
    @Test
    public void testLocalDirectoryValidation() throws IOException, InterruptedException{
        assumeNotNull(shadedJar);
        assertEquals( 0, exec( shadedJar, new String[]{"--" + IoosSosValidator.DIR, "src/test/resources/documents"} ) );
    }

    @Test
    public void testHttpValidation() throws IOException, InterruptedException{
        String url = System.getProperty("url");
        assumeNotNull(url);
        assertEquals( 0, exec( shadedJar, new String[]{"--" + IoosSosValidator.URL, url} ) );
    }
    
    @Test
    public void testKvpHttpValidation() throws IOException, InterruptedException{
        String kvpUrl = System.getProperty("kvpUrl");
        assumeNotNull(kvpUrl);
        assertEquals( 0, exec( shadedJar, new String[]{"--" + IoosSosValidator.KVP_URL, kvpUrl} ) );
    }

    @Test
    public void testPoxHttpValidation() throws IOException, InterruptedException{
        String poxUrl = System.getProperty("poxUrl");
        assumeNotNull(poxUrl);
        assertEquals( 0, exec( shadedJar, new String[]{"--" + IoosSosValidator.POX_URL, poxUrl} ) );
    }    
}
