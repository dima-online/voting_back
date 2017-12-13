package kz.bsbnb.common.bean;

import kz.bsbnb.common.model.VotingMessage;

import java.util.Date;
import java.util.Set;

/**
 * Created by ruslan on 15.11.16.
 */
public class RegVotingBean {

    private Long id;
    private String votingType;
    private Set<VotingMessage> messages;
    private Date dateCreate;
    private Date dateBegin;
    private Date dateEnd;
    private Long organisationId;
    private Long userId;

    public RegVotingBean() {
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

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

