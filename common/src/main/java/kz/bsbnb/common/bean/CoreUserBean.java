package kz.bsbnb.common.bean;

import kz.bsbnb.common.consts.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruslan on 17.11.16.
 */
public class CoreUserBean {

    private Long id;
    private List<Role> roles;
    private Role role;
    private String iin;
    private String email;
    private String phone;
    private String fullName;
    private Integer shareCount;
    private Long organisationId;

    public CoreUserBean() {
        this.roles = new ArrayList<>();
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

    public Integer getShareCount() {return shareCount;}

    public void setShareCount(Integer shareCount) {this.shareCount = shareCount;}

    public String getIin() {return iin;}

    public void setIin(String iin) {this.iin = iin;}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void delRole(Role role) {
        this.roles.remove(role);
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getFullName() {return fullName;}

    public void setFullName(String fullName) {this.fullName = fullName;}
}
