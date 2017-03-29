package kz.bsbnb.common.bean;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by ruslan on 08.12.16.
 */
public class RepVotingBean {

    private Long id;
    private String votingType;
    private String subject;
    private Date dateCreate;
    private Date dateBegin;
    private Date dateEnd;
    private Date dateClose;
    private String status;
    private Long organisationId;
    private String organisationName;
    private List<RepQuestionBean> repQuestionBeen;
    private Long lastReestrId;
    private Boolean kvoroom;

    public RepVotingBean() {
    }

    public Long getLastReestrId() {
        return lastReestrId;
    }

    public void setLastReestrId(Long lastReestrId) {
        this.lastReestrId = lastReestrId;
    }

    public List<RepQuestionBean> getRepQuestionBeen() {
        return repQuestionBeen;
    }

    public void setRepQuestionBeen(List<RepQuestionBean> repQuestionBeen) {
        this.repQuestionBeen = repQuestionBeen;
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

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public Boolean getKvoroom() {
        return kvoroom;
    }

    public void setKvoroom(Boolean kvoroom) {
        this.kvoroom = kvoroom;
    }
}
