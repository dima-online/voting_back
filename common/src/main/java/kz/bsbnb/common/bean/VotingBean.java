package kz.bsbnb.common.bean;

import java.util.Date;
import java.util.Set;

public class VotingBean {

    private Long id;
    private String votingType;
    private String subject;
    private Date dateCreate;
    private Date dateBegin;
    private Date dateEnd;
    private Date dateClose;
    private String status;
    private Date lastChanged;
    private Set<QuestionBean> questionSet;
    private Set<VoterBean> voterSet;
    private boolean canVote;
    private Long organisationId;
    private String organisationName;
    private Byte[] logo;
    private Integer questionCount;
    private Long lastReestrId;
    private Long shareCount;
    private Boolean kvoroom;

    public VotingBean() {
    }

    public Long getShareCount() {
        return shareCount;
    }

    public void setShareCount(Long shareCount) {
        this.shareCount = shareCount;
    }

    public Long getLastReestrId() {
        return lastReestrId;
    }

    public void setLastReestrId(Long lastReestrId) {
        this.lastReestrId = lastReestrId;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
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

    public Set<QuestionBean> getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(Set<QuestionBean> questionSet) {
        this.questionSet = questionSet;
    }

    public Set<VoterBean> getVoterSet() {
        return voterSet;
    }

    public void setVoterSet(Set<VoterBean> voterSet) {
        this.voterSet = voterSet;
    }

    public boolean isCanVote() {
        return canVote;
    }

    public void setCanVote(boolean canVote) {
        this.canVote = canVote;
    }

    public Boolean getKvoroom() {
        return kvoroom;
    }

    public void setKvoroom(Boolean kvoroom) {
        this.kvoroom = kvoroom;
    }

    public Byte[] getLogo() {
        return logo;
    }

    public void setLogo(Byte[] logo) {
        this.logo = logo;
    }
}
