/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.bsbnb.common.model;

import kz.bsbnb.common.util.Constants;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ruslan
 */
@Entity
@Table(name = "voter", schema = Constants.DB_SCHEMA_CORE)
public class Voter implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
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
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User userId;
    @JoinColumn(name = "voting_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Voting votingId;

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
