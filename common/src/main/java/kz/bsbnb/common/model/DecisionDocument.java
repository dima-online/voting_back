package kz.bsbnb.common.model;

import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by serik.mukashev on 11.12.2017.
 */
@Entity
@Table(name = "decision_document", schema = Constants.DB_SCHEMA_CORE)
public class DecisionDocument implements Serializable {
    @Id
    @SequenceGenerator(name = "core.decision_document_id_seq", sequenceName = "core.decision_document_id_seq", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "core.decision_document_id_seq")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "voting_id")
    private Voting voting;
    @ManyToOne
    @JoinColumn(name = "voter_id")
    private Voter voter;
    @ManyToOne
    @JoinColumn(name = "parent_user")
    private User parentUser;
    @Column(name = "json_document")
    @Size(max = 5_000_000)
    private String document;
    @Column(name = "signature")
    @Size(max = 20_000)
    private String signature;
    @Column(name = "public_key")
    private String publicKey;



    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Voting getVoting() {
        return voting;
    }

    public void setVoting(Voting voting) {
        this.voting = voting;
    }

    public Voter getVoter() {
        return voter;
    }

    public void setVoter(Voter voter) {
        this.voter = voter;
    }

    public User getParentUser() {
        return parentUser;
    }

    public void setParentUser(User parentUser) {
        this.parentUser = parentUser;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
