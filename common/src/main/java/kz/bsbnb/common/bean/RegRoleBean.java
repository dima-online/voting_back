package kz.bsbnb.common.bean;


/**
 * Created by ruslan on 07.12.16.
 */
public class RegRoleBean {
    private Long userId;
    private Long orgId;
    private String role;
    private ConfirmBean confirmBean;

    public RegRoleBean() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ConfirmBean getConfirmBean() {
        return confirmBean;
    }

    public void setConfirmBean(ConfirmBean confirmBean) {
        this.confirmBean = confirmBean;
    }
}
