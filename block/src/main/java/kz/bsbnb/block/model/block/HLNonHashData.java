package kz.bsbnb.block.model.block;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Olzhas.Pazyldayev on 04.10.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HLNonHashData {
    private Set<HLChaincodeEvent> chaincodeEvents = new HashSet<>();
    private HLTimestamp localLedgerCommitTimestamp;

    public HLNonHashData() {
    }

    public Set<HLChaincodeEvent> getChaincodeEvents() {
        return chaincodeEvents;
    }

    public void setChaincodeEvents(Set<HLChaincodeEvent> chaincodeEvents) {
        this.chaincodeEvents = chaincodeEvents;
    }

    public HLTimestamp getLocalLedgerCommitTimestamp() {
        return localLedgerCommitTimestamp;
    }

    public void setLocalLedgerCommitTimestamp(HLTimestamp localLedgerCommitTimestamp) {
        this.localLedgerCommitTimestamp = localLedgerCommitTimestamp;
    }
}
