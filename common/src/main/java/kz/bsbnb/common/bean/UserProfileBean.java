package kz.bsbnb.common.bean;

import java.util.List;

/**
 * Created by ruslan on 16.11.16.
 */
public class UserProfileBean {

    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String iin;
    private List<UserOrgBean> beanList;

    public UserProfileBean() {
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public List<UserOrgBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<UserOrgBean> beanList) {
        this.beanList = beanList;
    }
}
