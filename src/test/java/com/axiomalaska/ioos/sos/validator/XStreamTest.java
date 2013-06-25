package com.axiomalaska.ioos.sos.validator;

import static org.junit.Assert.*;

import org.junit.Test;

import com.axiomalaska.ioos.sos.validator.provider.http.config.RequestConfiguration;
import com.axiomalaska.ioos.sos.validator.xstream.XStreamRepository;

public class XStreamTest {
    @Test
    public void testXStreamSerialization() {
        RequestConfiguration config = RequestConfiguration.exampleConfig();
        String xml = XStreamRepository.instance().toXML(config);
        Object obj = XStreamRepository.instance().fromXML(xml);
        assertTrue(obj instanceof RequestConfiguration);
        RequestConfiguration configAgain = (RequestConfiguration) obj;
        assertEquals(config, configAgain);
    }
}
