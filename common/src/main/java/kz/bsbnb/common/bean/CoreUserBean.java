package kz.bsbnb.common.bean;

import kz.bsbnb.common.consts.Role;

/**
 * Created by ruslan on 17.11.16.
 */
public class CoreUserBean {

    private Long id;
    private Role role;
    private String iin;
    private String email;
    private String phone;
    private String fullName;
    private int shareCount;
    private Long organisationId;

    public CoreUserBean() {
    }

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getShareCount() {return shareCount;}

    public void setShareCount(int shareCount) {this.shareCount = shareCount;}

    public String getIin() {return iin;}

    public void setIin(String iin) {this.iin = iin;}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getFullName() {return fullName;}

    public void setFullName(String fullName) {this.fullName = fullName;}
}
