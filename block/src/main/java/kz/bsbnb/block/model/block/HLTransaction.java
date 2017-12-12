package kz.bsbnb.block.model.block;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Olzhas.Pazyldayev on 04.10.2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HLTransaction {
    private Integer type;
    private String chaincodeID;
    private String payload;
    private String txid;
    private HLTimestamp timestamp;
    private String error;

    public HLTransaction() {
    }

    public HLTransaction(Integer type, String chaincodeID, String payload, String txid, HLTimestamp timestamp) {
        this.type = type;
        this.chaincodeID = chaincodeID;
        this.payload = payload;
        this.txid = txid;
        this.timestamp = timestamp;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getChaincodeID() {
        return chaincodeID;
    }

    public void setChaincodeID(String chaincodeID) {
        this.chaincodeID = chaincodeID;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public HLTimestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(HLTimestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
