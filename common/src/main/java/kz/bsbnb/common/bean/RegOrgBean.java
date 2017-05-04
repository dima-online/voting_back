package kz.bsbnb.common.bean;

/**
 * Created by ruslan on 23.11.16.
 */
public class RegOrgBean {

    private Long id;
    private String organisationName;
    private String organisationNum; //БИН
    private String externalId;
    private String status;
    private Long allShareCount;
    private Integer userCount;
    private Integer votingCount;
    private Integer closedVotingCount;
    private String address;
    private String email;
    private String phone;
    private String executiveName;
    private String executiveId;

    public RegOrgBean() {
    }

    public String getExecutiveId() {
        return executiveId;
    }

    public void setExecutiveId(String executiveId) {
        this.executiveId = executiveId;
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

    public String getExecutiveName() {
        return executiveName;
    }

    public void setExecutiveName(String executiveName) {
        this.executiveName = executiveName;
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

    public Long getAllShareCount() {
        return allShareCount;
    }

    public void setAllShareCount(Long allShareCount) {
        this.allShareCount = allShareCount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
