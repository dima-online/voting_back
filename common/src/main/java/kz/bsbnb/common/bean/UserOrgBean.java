package kz.bsbnb.common.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruslan on 17.11.16.
 */
public class UserOrgBean {
    private Long userId;
    private Long organisationId;
    private List<String> roles;
    private String role;
    private String organisationName;
    private Integer shareCount;
    private double sharePercent;

    public UserOrgBean() {
        this.roles = new ArrayList<>();
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

    public List<String> getRoles() {
        return roles;
    }

    public void addRole(String role) {
        this.roles.add(role);
    }

    public void delRole(String role) {
        this.roles.remove(role);
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public double getSharePercent() {
        return sharePercent;
    }

    public void setSharePercent(double sharePercent) {
        this.sharePercent = sharePercent;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
