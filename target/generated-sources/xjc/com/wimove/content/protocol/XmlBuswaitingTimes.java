//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.03.03 at 11:31:31 PM CET 
//


package com.wimove.content.protocol;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://content.wimove.com/services}busWaitingTime" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "busWaitingTime"
})
@XmlRootElement(name = "buswaitingTimes")
public class XmlBuswaitingTimes {

    @XmlElement(required = true)
    protected List<XmlBusWaitingTime> busWaitingTime;

    /**
     * Gets the value of the busWaitingTime property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the busWaitingTime property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBusWaitingTime().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlBusWaitingTime }
     * 
     * 
     */
    public List<XmlBusWaitingTime> getBusWaitingTime() {
        if (busWaitingTime == null) {
            busWaitingTime = new ArrayList<XmlBusWaitingTime>();
        }
        return this.busWaitingTime;
    }

}
