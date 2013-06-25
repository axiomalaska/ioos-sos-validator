package com.axiomalaska.ioos.sos.validator.xstream;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DateTimeConverter implements Converter {
    public boolean canConvert(Class type) {
        return type.equals(DateTime.class);
    }
   
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer,
            MarshallingContext context) {
        DateTime dateTime = (DateTime) source;
        writer.setValue(dateTime.toString());        
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader,
            UnmarshallingContext context) {
        return new DateTime(reader.getValue(), DateTimeZone.UTC);
    }
}
