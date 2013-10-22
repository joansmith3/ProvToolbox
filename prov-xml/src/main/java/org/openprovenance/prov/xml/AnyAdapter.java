//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-792 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.05 at 11:52:42 PM GMT 
//

package org.openprovenance.prov.xml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.openprovenance.prov.model.DOMProcessing;

/** Adapter convert an Attribute to DOM and vice-versa.
 * @author lavm
 * @see javax.xml.bind.annotation.adapters.XmlAdapter
 * 
 */
public class AnyAdapter extends
        XmlAdapter<Object, org.openprovenance.prov.model.Attribute> {

    ProvFactory pFactory = new ProvFactory();

    ValueConverter vconv = new ValueConverter(pFactory);


    /** Unmarshals an Object (expect to be a DOM Element) into an Attribute.
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    public org.openprovenance.prov.model.Attribute unmarshal(Object value) {
        // System.out.println("AnyAdapter unmarshalling for " + value);
        if (value instanceof org.w3c.dom.Element) {
            org.w3c.dom.Element el = (org.w3c.dom.Element) value;
            return DOMProcessing.unmarshallAttribute(el,pFactory,vconv);
        }
        if (value instanceof JAXBElement) {
            JAXBElement<?> je = (JAXBElement<?>) value;
            return pFactory.newAttribute(je.getName(), je.getValue(), vconv);
        }
        return null;
    }

    
    /** Marshals an Attribute to a DOM Element.
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     */
    public Object marshal(org.openprovenance.prov.model.Attribute attribute) {
        return DOMProcessing.marshalAttribute(attribute);
    }
   
}
