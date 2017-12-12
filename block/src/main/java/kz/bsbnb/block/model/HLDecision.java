package kz.bsbnb.block.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by Olzhas.Pazyldayev on 12.12.2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HLDecision implements Serializable {

    private String decisionDocumentHash;

    private String voterIin;

    private String accountNumber;

    private String votingId;

    public HLDecision() {
    }

    public HLDecision(String decisionDocumentHash, String voterIin, String accountNumber, String votingId) {
        this.decisionDocumentHash = decisionDocumentHash;
        this.voterIin = voterIin;
        this.accountNumber = accountNumber;
        this.votingId = votingId;
    }

    public String getDecisionDocumentHash() {
        return decisionDocumentHash;
    }

    public void setDecisionDocumentHash(String decisionDocumentHash) {
        this.decisionDocumentHash = decisionDocumentHash;
    }

    public String getVoterIin() {
        return voterIin;
    }

    public void setVoterIin(String voterIin) {
        this.voterIin = voterIin;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getVotingId() {
        return votingId;
    }

    public void setVotingId(String votingId) {
        this.votingId = votingId;
    }
}
