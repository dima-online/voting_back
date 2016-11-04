package kz.bsbnb.common.bean;

import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.common.model.UserInfo;

/**
 * Created by ruslan on 19/10/2016.
 */
public class UserBean {

    private Long id;
    private Role role;
    private String login;
    private String iin;
    private UserInfo userInfo;
    private Organisation organisation;
    private int shareCount;

    public UserBean() {
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
