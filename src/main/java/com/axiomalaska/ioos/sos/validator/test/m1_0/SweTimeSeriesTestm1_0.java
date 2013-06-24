package com.axiomalaska.ioos.sos.validator.test.m1_0;

import com.axiomalaska.ioos.sos.validator.provider.SosDocumentProvider;
import com.axiomalaska.ioos.sos.validator.provider.SosDocumentType;
import com.axiomalaska.ioos.sos.validator.test.AbstractSweDataRecordTest;

public class SweTimeSeriesTestm1_0 extends AbstractSweDataRecordTest{
    public SweTimeSeriesTestm1_0(SosDocumentProvider provider) {
        super(provider);
    }
    
    @Override
    public SosDocumentType getSosDocumentType() {
        return SosDocumentType.M1_0_SWE_TIME_SERIES;
    }
}