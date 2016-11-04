/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.bsbnb.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author ruslan
 */
@Entity
@Table(name = "voter", schema = Constants.DB_SCHEMA_CORE)
public class Voter implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "core.voter_id_seq", sequenceName = "core.voter_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "core.voter_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_adding")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdding;
    @Basic(optional = false)
    @NotNull
    @Column(name = "share_count")
    private int shareCount;
    @Size(max = 200)
    @Column(name = "signature")
    private String signature;
    @Size(max = 2000)
    @Column(name = "public_key")
    private String publicKey;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "voterId", fetch = FetchType.EAGER)
    private Set<Decision> decisionSet;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User userId;
    @JoinColumn(name = "voting_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Voting votingId;
    @Column(name = "date_voting")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateVoting;

    public Voter() {
    }

    public Voter(Long id) {
        this.id = id;
    }

    public Voter(Long id, int shareCount) {
        this.id = id;
        this.shareCount = shareCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateAdding() {
        return dateAdding;
    }

    public void setDateAdding(Date dateAdding) {
        this.dateAdding = dateAdding;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
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

    @XmlTransient
    public Set<Decision> getDecisionSet() { return decisionSet;}

    public void setDecisionSet(Set<Decision> decisionSet) { this.decisionSet = decisionSet;}

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Voting getVotingId() {
        return votingId;
    }

    public void setVotingId(Voting votingId) {
        this.votingId = votingId;
    }

    public Date getDateVoting() {return dateVoting;}

    public void setDateVoting(Date dateVoting) {this.dateVoting = dateVoting;}

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Voter)) {
            return false;
        }
        Voter other = (Voter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "kz.bsbnb.common.model.Voter[ id=" + id + " ]";
    }
    
}
