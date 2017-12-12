package kz.bsbnb.block.model.command;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Olzhas.Pazyldayev on 23.08.2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HLParameter {
    private Integer type;
    private HLChainCodeID chaincodeID;
    private HLCtorMsg ctorMsg;
    private String secureContext;

    public HLParameter() {
    }

    public HLParameter(Integer type, HLChainCodeID chaincodeID, HLCtorMsg ctorMsg, String secureContext) {
        this.type = type;
        this.chaincodeID = chaincodeID;
        this.ctorMsg = ctorMsg;
        this.secureContext = secureContext;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public HLChainCodeID getChaincodeID() {
        return chaincodeID;
    }

    public void setChaincodeID(HLChainCodeID chaincodeID) {
        this.chaincodeID = chaincodeID;
    }

    public HLCtorMsg getCtorMsg() {
        return ctorMsg;
    }

    public void setCtorMsg(HLCtorMsg ctorMsg) {
        this.ctorMsg = ctorMsg;
    }

    public String getSecureContext() {
        return secureContext;
    }

    public void setSecureContext(String secureContext) {
        this.secureContext = secureContext;
    }
}
