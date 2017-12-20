/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.bsbnb.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kz.bsbnb.common.consts.Locale;
import kz.bsbnb.common.consts.VotingType;
import kz.bsbnb.common.util.Constants;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ruslan
 */
@Entity
@Table(name = "voting", schema = Constants.DB_SCHEMA_CORE)
public class Voting implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "voting_type")
    @Enumerated(EnumType.STRING)
    private VotingType votingType;

    @BatchSize(size = 5)
    @OneToMany(mappedBy = "voting", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "votingMessages")
    @OrderBy("locale ASC")
    private Set<VotingMessage> messages = new HashSet<>();

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
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "voting", fetch = FetchType.EAGER)
    private Set<Question> questionSet;
    @JsonIgnore
    @JoinColumn(name = "organisation_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "voting_organisation_fk"))
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Organisation organisation;
    @JsonIgnore
    @JoinColumn(name = "who_changed", referencedColumnName = "id", foreignKey = @ForeignKey(name = "voting_user_who_changed_fk"))
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User whoChanged;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "voting", fetch = FetchType.EAGER)
    private Set<Voter> voterSet;
    @Column(name = "last_reestr_id")
    private Long lastReestrId;
    @Column(name = "kvoroom")
    private Boolean kvoroom;
    @Transient
    private Boolean hasQuestions;

    public Voting() {
    }

    public Voting(Long id) {
        this.id = id;
    }

    public Voting(Long id, VotingType votingType, Set<VotingMessage> messages, Date dateCreate, String status) {
        this.id = id;
        this.votingType = votingType;
        this.messages = messages;
        this.dateCreate = dateCreate;
        this.status = status;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLastReestrId() {
        return lastReestrId;
    }

    public void setLastReestrId(Long lastReestrId) {
        this.lastReestrId = lastReestrId;
    }

    public String getVotingType() {
        return votingType.toString();
    }

    public void setVotingType(VotingType votingType) {
        this.votingType = votingType;
    }

    public Set<VotingMessage> getMessages() {
        return messages;
    }

    public void setMessages(Set<VotingMessage> messages) {
        this.messages = messages;
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

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public User getWhoChanged() {
        return whoChanged;
    }

    public void setWhoChanged(User whoChanged) {
        this.whoChanged = whoChanged;
    }

    public Boolean getKvoroom() {
        return kvoroom;
    }

    public void setKvoroom(Boolean kvoroom) {
        this.kvoroom = kvoroom;
    }

    public Boolean getHasQuestions() {
        return hasQuestions;
    }

    public void setHasQuestions(Boolean hasQuestions) {
        this.hasQuestions = hasQuestions;
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

    @JsonIgnore
    public VotingMessage getMessage(Locale locale) {
        try {
            return getMessages().parallelStream()
                    .filter(votingMessage -> votingMessage.getLocale().equals(locale)).findFirst().get();
        } catch (Exception e) {
        }
        return null;
    }
}
