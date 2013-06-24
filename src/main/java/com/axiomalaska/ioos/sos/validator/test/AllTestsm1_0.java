package com.axiomalaska.ioos.sos.validator.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.axiomalaska.ioos.sos.validator.test.m1_0.CapabilitiesTestm1_0;
import com.axiomalaska.ioos.sos.validator.test.m1_0.NetworkSensorMLTestm1_0;
import com.axiomalaska.ioos.sos.validator.test.m1_0.ObservationCollectionTestm1_0;
import com.axiomalaska.ioos.sos.validator.test.m1_0.StationSensorMLTestm1_0;
import com.axiomalaska.ioos.sos.validator.test.m1_0.SweTimeSeriesProfileTestm1_0;
import com.axiomalaska.ioos.sos.validator.test.m1_0.SweTimeSeriesTestm1_0;

@RunWith(Suite.class)
@Suite.SuiteClasses({CapabilitiesTestm1_0.class,
    NetworkSensorMLTestm1_0.class, StationSensorMLTestm1_0.class,
    ObservationCollectionTestm1_0.class,
    SweTimeSeriesTestm1_0.class, SweTimeSeriesProfileTestm1_0.class})
public class AllTestsm1_0 {

}
