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
    private Integer allShareCount;
    private String address;
    private String email;
    private String phone;

    public RegOrgBean() {
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

    public Integer getAllShareCount() {
        return allShareCount;
    }

    public void setAllShareCount(Integer allShareCount) {
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
