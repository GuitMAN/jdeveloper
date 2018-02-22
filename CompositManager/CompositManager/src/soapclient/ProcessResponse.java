
package soapclient;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="Composite">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="Revision" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="State" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="Mode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="Default" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="DeployedTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "composite" })
@XmlRootElement(name = "processResponse")
public class ProcessResponse {

    @XmlElement(name = "Composite", required = true)
    protected ArrayList<ProcessResponse.Composite> composite;

    /**
     * Gets the value of the composite property.
     *
     * @return
     *     possible object is
     *     {@link ProcessResponse.Composite }
     *
     */
    public ArrayList<ProcessResponse.Composite> getComposite() {
        return composite;
    }

    /**
     * Sets the value of the composite property.
     *
     * @param value
     *     allowed object is
     *     {@link ProcessResponse.Composite }
     *
     */
    public void setComposite(ArrayList<ProcessResponse.Composite> value) {
        this.composite = value;
    }


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
     *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="Revision" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="State" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="Mode" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="Default" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="DeployedTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "",
             propOrder = { "name", "revision", "state", "mode", "_default",
                           "deployedTime" })
    public static class Composite {

        @XmlElement(name = "Name", required = true)
        protected String name;
        @XmlElement(name = "Revision", required = true)
        protected String revision;
        @XmlElement(name = "State", required = true)
        protected String state;
        @XmlElement(name = "Mode", required = true)
        protected String mode;
        @XmlElement(name = "Default")
        protected boolean _default;
        @XmlElement(name = "DeployedTime", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar deployedTime;

        /**
         * Gets the value of the name property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the revision property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getRevision() {
            return revision;
        }

        /**
         * Sets the value of the revision property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setRevision(String value) {
            this.revision = value;
        }

        /**
         * Gets the value of the state property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getState() {
            return state;
        }

        /**
         * Sets the value of the state property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setState(String value) {
            this.state = value;
        }

        /**
         * Gets the value of the mode property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getMode() {
            return mode;
        }

        /**
         * Sets the value of the mode property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setMode(String value) {
            this.mode = value;
        }

        /**
         * Gets the value of the default property.
         *
         */
        public boolean isDefault() {
            return _default;
        }

        /**
         * Sets the value of the default property.
         *
         */
        public void setDefault(boolean value) {
            this._default = value;
        }

        /**
         * Gets the value of the deployedTime property.
         *
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public XMLGregorianCalendar getDeployedTime() {
            return deployedTime;
        }

        /**
         * Sets the value of the deployedTime property.
         *
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public void setDeployedTime(XMLGregorianCalendar value) {
            this.deployedTime = value;
        }

    }

}
