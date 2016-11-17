package kz.bsbnb.common.bean;

import java.util.Date;

/**
 * Created by ruslan on 10.11.16.
 */
public class MessageBean {

    private Long id;
    private String subject;
    private String body;
    private Date dateCreate;
    private Date dateRead;
    private Long parentId;
    private Long userId;
    private Long organisationId;
    private String organisationName;
    private Boolean fromUser;

    public MessageBean() {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }

    public Boolean getFromUser() {
        return fromUser;
    }

    public void setFromUser(Boolean fromUser) {
        this.fromUser = fromUser;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String  getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }
}
