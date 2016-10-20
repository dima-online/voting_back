package kz.bsbnb.common.bean;

import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.UserInfo;

/**
 * Created by ruslan on 19/10/2016.
 */
public class UserBean {

    private Long id;
    private Role role;
    private User user;
    private UserInfo userInfo;
    private Organisation organisation;

    public UserBean() {
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }
}
