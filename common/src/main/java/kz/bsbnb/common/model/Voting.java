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
@Table(name = "voting", schema = Constants.DB_SCHEMA_CORE)
public class Voting implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "core.voting_id_seq", sequenceName = "core.voting_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "core.voting_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "voting_type")
    private String votingType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "subject")
    private String subject;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_create")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreate;
    @Column(name = "date_begin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateBegin;
    @Column(name = "date_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnd;
    @Column(name = "date_close")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateClose;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "status")
    private String status;
    @Column(name = "last_changed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastChanged;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "votingId", fetch = FetchType.EAGER)
    private Set<Question> questionSet;
    @JsonIgnore
    @JoinColumn(name = "organisation_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Organisation organisationId;
    @JsonIgnore
    @JoinColumn(name = "who_changed", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User whoChanged;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "votingId", fetch = FetchType.EAGER)
    private Set<Voter> voterSet;

    public Voting() {
    }

    public Voting(Long id) {
        this.id = id;
    }

    public Voting(Long id, String votingType, String subject, Date dateCreate, String status) {
        this.id = id;
        this.votingType = votingType;
        this.subject = subject;
        this.dateCreate = dateCreate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVotingType() {
        return votingType;
    }

    public void setVotingType(String votingType) {
        this.votingType = votingType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Date getDateClose() {
        return dateClose;
    }

    public void setDateClose(Date dateClose) {
        this.dateClose = dateClose;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        this.lastChanged = lastChanged;
    }

    @XmlTransient
    public Set<Question> getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(Set<Question> questionSet) {
        this.questionSet = questionSet;
    }

    public Organisation getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Organisation organisationId) {
        this.organisationId = organisationId;
    }

    public User getWhoChanged() {
        return whoChanged;
    }

    public void setWhoChanged(User whoChanged) {
        this.whoChanged = whoChanged;
    }

    @XmlTransient
    public Set<Voter> getVoterSet() {
        return voterSet;
    }

    public void setVoterSet(Set<Voter> voterSet) {
        this.voterSet = voterSet;
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
        if (!(object instanceof Voting)) {
            return false;
        }
        Voting other = (Voting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "kz.bsbnb.common.model.Voting[ id=" + id + " ]";
    }
    
}
