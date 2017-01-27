/**
 * TResponseRegistry.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package kz.bsbnb.util.ERCBService;

public class TResponseRegistry  implements java.io.Serializable {
    private kz.bsbnb.util.ERCBService.TRegistry registry;

    private int errorCode;

    private java.lang.String errorText;

    public TResponseRegistry() {
    }

    public TResponseRegistry(
           kz.bsbnb.util.ERCBService.TRegistry registry,
           int errorCode,
           java.lang.String errorText) {
           this.registry = registry;
           this.errorCode = errorCode;
           this.errorText = errorText;
    }


    /**
     * Gets the registry value for this TResponseRegistry.
     * 
     * @return registry
     */
    public kz.bsbnb.util.ERCBService.TRegistry getRegistry() {
        return registry;
    }


    /**
     * Sets the registry value for this TResponseRegistry.
     * 
     * @param registry
     */
    public void setRegistry(kz.bsbnb.util.ERCBService.TRegistry registry) {
        this.registry = registry;
    }


    /**
     * Gets the errorCode value for this TResponseRegistry.
     * 
     * @return errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }


    /**
     * Sets the errorCode value for this TResponseRegistry.
     * 
     * @param errorCode
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }


    /**
     * Gets the errorText value for this TResponseRegistry.
     * 
     * @return errorText
     */
    public java.lang.String getErrorText() {
        return errorText;
    }


    /**
     * Sets the errorText value for this TResponseRegistry.
     * 
     * @param errorText
     */
    public void setErrorText(java.lang.String errorText) {
        this.errorText = errorText;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TResponseRegistry)) return false;
        TResponseRegistry other = (TResponseRegistry) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.registry==null && other.getRegistry()==null) || 
             (this.registry!=null &&
              this.registry.equals(other.getRegistry()))) &&
            this.errorCode == other.getErrorCode() &&
            ((this.errorText==null && other.getErrorText()==null) || 
             (this.errorText!=null &&
              this.errorText.equals(other.getErrorText())));
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
        if (getRegistry() != null) {
            _hashCode += getRegistry().hashCode();
        }
        _hashCode += getErrorCode();
        if (getErrorText() != null) {
            _hashCode += getErrorText().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TResponseRegistry.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:ERCBServices.Objects.Voting.Core", "TResponseRegistry"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registry");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Registry"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:ERCBServices.Objects.Voting.Core", "TRegistry"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errorCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ErrorCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errorText");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ErrorText"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
