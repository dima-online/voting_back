/**
 * IERCBVotingServices.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package kz.bsbnb.util.ERCBService;

public interface IERCBVotingServices extends java.rmi.Remote {
    public kz.bsbnb.util.ERCBService.TResponseRegistry getRegistry(java.lang.String AIDN, java.lang.String AOrderDate) throws java.rmi.RemoteException;
    public int existsRegistry(java.lang.String AIDN, java.lang.String AOrderDate) throws java.rmi.RemoteException;
    public java.lang.String getChief(java.lang.String AIDN) throws java.rmi.RemoteException;
}
