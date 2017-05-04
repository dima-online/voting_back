package kz.bsbnb.common.bean;

import java.util.List;

/**
 * Created by ruslan on 28.10.16.
 */
public class OrgBean {
    private Long id;
    private String organisationName;
    private String organisationNum;
    private String externalId;
    private String status;
    private List<VotingBean> votingSet;
    private Integer shareCount;
    private Double sharePercent;
    private Long allShareCount;
    private Integer userCount;
    private Integer votingCount;
    private Integer closedVotingCount;
    private String executiveName;
    private String email;
    private String address;
    private String phone;

    public OrgBean() {
    }

    public Double getSharePercent() {
        return sharePercent;
    }

    public void setSharePercent(Double sharePercent) {
        this.sharePercent = sharePercent;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getVotingCount() {
        return votingCount;
    }

    public void setVotingCount(Integer votingCount) {
        this.votingCount = votingCount;
    }

    public Integer getClosedVotingCount() {
        return closedVotingCount;
    }

    public void setClosedVotingCount(Integer closedVotingCount) {
        this.closedVotingCount = closedVotingCount;
    }

    public Integer getShareCount() {return shareCount;}

    public void setShareCount(Integer shareCount) {this.shareCount = shareCount;}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getOrganisationNum() {
        return organisationNum;
    }

    public void setOrganisationNum(String organisationNum) {
        this.organisationNum = organisationNum;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<VotingBean> getVotingSet() {
        return votingSet;
    }

    public void setVotingSet(List<VotingBean> votingSet) {
        this.votingSet = votingSet;
    }

    public Long getAllShareCount() {
        return allShareCount==null?0:allShareCount;
    }

    public void setAllShareCount(Long allShareCount) {
        this.allShareCount = allShareCount;
    }

    public String getExecutiveName() {
        return executiveName;
    }

    public void setExecutiveName(String executiveName) {
        this.executiveName = executiveName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "OrgBean{" +
                "id=" + id +
                ", organisationName='" + organisationName + '\'' +
                ", organisationNum='" + organisationNum + '\'' +
                ", externalId='" + externalId + '\'' +
                ", status='" + status + '\'' +
                ", votingSet=" + votingSet +
                ", shareCount=" + shareCount +
                ", sharePercent=" + sharePercent +
                ", allShareCount=" + allShareCount +
                ", userCount=" + userCount +
                ", votingCount=" + votingCount +
                ", closedVotingCount=" + closedVotingCount +
                ", executiveName='" + executiveName + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
