package kz.bsbnb.block.model.block;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

/**
 * Created by Olzhas.Pazyldayev on 04.10.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HLBlock {
    private Integer version;
    private Set<HLTransaction> transactions;
    private String stateHash;
    private String previousBlockHash;
    private HLNonHashData nonHashData;

    public HLBlock() {
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Set<HLTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<HLTransaction> transactions) {
        this.transactions = transactions;
    }

    public String getStateHash() {
        return stateHash;
    }

    public void setStateHash(String stateHash) {
        this.stateHash = stateHash;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public void setPreviousBlockHash(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }

    public HLNonHashData getNonHashData() {
        return nonHashData;
    }

    public void setNonHashData(HLNonHashData nonHashData) {
        this.nonHashData = nonHashData;
    }
}
