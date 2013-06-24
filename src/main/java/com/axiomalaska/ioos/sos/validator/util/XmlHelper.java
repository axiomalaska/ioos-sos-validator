package com.axiomalaska.ioos.sos.validator.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Node;

public class XmlHelper {
    @SuppressWarnings("unchecked")
    public static <T> List<T> getChildren( XmlObject parent, Class<T> clazz) throws XmlException{
        List<T> children = new ArrayList<T>();

        int len = parent.getDomNode().getChildNodes().getLength();
        for ( int i = 0; i < len; i++ ) {
            Node item = parent.getDomNode().getChildNodes().item(i);
            XmlObject xbItem = XmlObject.Factory.parse(item);
            if (clazz.isAssignableFrom(xbItem.getClass())){
                children.add((T) xbItem);
            }
        }
        return children;
    }
}
