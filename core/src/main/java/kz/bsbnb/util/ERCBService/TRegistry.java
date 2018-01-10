/**
 * TRegistry.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package kz.bsbnb.util.ERCBService;


public class TRegistry  implements java.io.Serializable {
    private java.util.Date registryDate;

    private java.lang.String issuerIDN;

    private java.lang.String issuerName;

    private TShareholder[] shareholders;

    public TRegistry() {
    }

    public TRegistry(
            java.util.Date registryDate,
            java.lang.String issuerIDN,
            java.lang.String issuerName,
            TShareholder[] shareholders) {
        this.registryDate = registryDate;
        this.issuerIDN = issuerIDN;
        this.issuerName = issuerName;
        this.shareholders = shareholders;
    }


    /**
     * Gets the registryDate value for this TRegistry.
     *
     * @return registryDate
     */
    public java.util.Date getRegistryDate() {
        return registryDate;
    }


    /**
     * Sets the registryDate value for this TRegistry.
     *
     * @param registryDate
     */
    public void setRegistryDate(java.util.Date registryDate) {
        this.registryDate = registryDate;
    }


    /**
     * Gets the issuerIDN value for this TRegistry.
     *
     * @return issuerIDN
     */
    public java.lang.String getIssuerIDN() {
        return issuerIDN;
    }


    /**
     * Sets the issuerIDN value for this TRegistry.
     *
     * @param issuerIDN
     */
    public void setIssuerIDN(java.lang.String issuerIDN) {
        this.issuerIDN = issuerIDN;
    }


    /**
     * Gets the issuerName value for this TRegistry.
     *
     * @return issuerName
     */
    public java.lang.String getIssuerName() {
        return issuerName;
    }


    /**
     * Sets the issuerName value for this TRegistry.
     *
     * @param issuerName
     */
    public void setIssuerName(java.lang.String issuerName) {
        this.issuerName = issuerName;
    }


    /**
     * Gets the shareholders value for this TRegistry.
     *
     * @return shareholders
     */
    public TShareholder[] getShareholders() {
        return shareholders;
    }


    /**
     * Sets the shareholders value for this TRegistry.
     *
     * @param shareholders
     */
    public void setShareholders(TShareholder[] shareholders) {
        this.shareholders = shareholders;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TRegistry)) return false;
        TRegistry other = (TRegistry) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
                ((this.registryDate==null && other.getRegistryDate()==null) ||
                        (this.registryDate!=null &&
                                this.registryDate.equals(other.getRegistryDate()))) &&
                ((this.issuerIDN==null && other.getIssuerIDN()==null) ||
                        (this.issuerIDN!=null &&
                                this.issuerIDN.equals(other.getIssuerIDN()))) &&
                ((this.issuerName==null && other.getIssuerName()==null) ||
                        (this.issuerName!=null &&
                                this.issuerName.equals(other.getIssuerName()))) &&
                ((this.shareholders==null && other.getShareholders()==null) ||
                        (this.shareholders!=null &&
                                java.util.Arrays.equals(this.shareholders, other.getShareholders())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getRegistryDate() != null) {
            _hashCode += getRegistryDate().hashCode();
        }
        if (getIssuerIDN() != null) {
            _hashCode += getIssuerIDN().hashCode();
        }
        if (getIssuerName() != null) {
            _hashCode += getIssuerName().hashCode();
        }
        if (getShareholders() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getShareholders());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getShareholders(), i);
                if (obj != null &&
                        !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
            new org.apache.axis.description.TypeDesc(TRegistry.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:ERCBServices.Objects.Voting.Core", "TRegistry"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registryDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RegistryDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("issuerIDN");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IssuerIDN"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("issuerName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IssuerName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shareholders");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Shareholders"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:ERCBServices.Objects.Voting.Core", "TShareholder"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
            java.lang.String mechType,
            java.lang.Class _javaType,
            javax.xml.namespace.QName _xmlType) {
        return
                new  org.apache.axis.encoding.ser.BeanSerializer(
                        _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
            java.lang.String mechType,
            java.lang.Class _javaType,
            javax.xml.namespace.QName _xmlType) {
        return
                new  org.apache.axis.encoding.ser.BeanDeserializer(
                        _javaType, _xmlType, typeDesc);
    }

}
