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
    private Integer allShareCount;
    private Integer userCount;
    private Integer votingCount;
    private Integer closedVotingCount;

    public OrgBean() {
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

    public Integer getAllShareCount() {
        return allShareCount==null?0:allShareCount;
    }

    public void setAllShareCount(Integer allShareCount) {
        this.allShareCount = allShareCount;
    }
}
