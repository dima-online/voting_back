/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.bsbnb.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kz.bsbnb.common.consts.Locale;
import kz.bsbnb.common.consts.Scope;
import kz.bsbnb.common.util.Constants;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Olzhas.Pazyldayev on 22.12.2017
 */
@Entity
@Table(name = "theme", schema = Constants.DB_SCHEMA_CORE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Theme implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @BatchSize(size = 5)
    @OneToMany(mappedBy = "theme", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "themeMessages")
    @OrderBy("locale ASC")
    private Set<ThemeMessage> messages = new HashSet<>();

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "voting_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "theme_voting_fk"))
    private Voting voting;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(name = "scope")
    @Enumerated(EnumType.STRING)
    private Scope scope = Scope.PUBLIC;

    @Column(name = "create_time")
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT_BOOTSTRAP)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "end_time")
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT_BOOTSTRAP)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "theme_user_fk"))
    private User author;

    @Transient
    private Integer newMessage;

    public Theme() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Set<ThemeMessage> getMessages() {
        return messages;
    }

    public void setMessages(Set<ThemeMessage> messages) {
        this.messages = messages;
    }

    public Voting getVoting() {
        return voting;
    }

    public void setVoting(Voting voting) {
        this.voting = voting;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Integer getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(Integer newMessage) {
        this.newMessage = newMessage;
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
        if (!(object instanceof Theme)) {
            return false;
        }
        Theme other = (Theme) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "kz.bsbnb.common.model.Question[ id=" + id + " ]";
    }

    @JsonIgnore
    public ThemeMessage getMessage(Locale locale) {
        try {
            return getMessages().parallelStream()
                    .filter(themeMessage -> themeMessage.getLocale().equals(locale)).findFirst().get();
        } catch (Exception e) {
        }
        return null;
    }


}
