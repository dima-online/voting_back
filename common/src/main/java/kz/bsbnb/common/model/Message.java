/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.bsbnb.common.model;

import kz.bsbnb.common.util.Constants;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ruslan
 */
@Entity
@Table(name = "message", schema = Constants.DB_SCHEMA_CORE)
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Size(max = 2000)
    @Column(name = "subject")
    private String subject;
    @Size(max = 20000)
    @Column(name = "body")
    private String body;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_create")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreate;
    @Column(name = "date_read")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRead;
    @OneToMany(mappedBy = "parentId", fetch = FetchType.EAGER)
    private Set<Message> messageSet;
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Message parentId;
    @JoinColumn(name = "organisation_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Organisation organisationId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User userId;

    public Message() {
    }

    public Message(Long id) {
        this.id = id;
    }

    public Message(Long id, Date dateCreate) {
        this.id = id;
        this.dateCreate = dateCreate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateRead() {
        return dateRead;
    }

    public void setDateRead(Date dateRead) {
        this.dateRead = dateRead;
    }

    @XmlTransient
    public Set<Message> getMessageSet() {
        return messageSet;
    }

    public void setMessageSet(Set<Message> messageSet) {
        this.messageSet = messageSet;
    }

    public Message getParentId() {
        return parentId;
    }

    public void setParentId(Message parentId) {
        this.parentId = parentId;
    }

    public Organisation getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Organisation organisationId) {
        this.organisationId = organisationId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
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
        if (!(object instanceof Message)) {
            return false;
        }
        Message other = (Message) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "kz.bsbnb.common.model.Message[ id=" + id + " ]";
    }
    
}
