package kz.bsbnb.common.bean;

/**
 * Created by ruslan on 19/10/2016.
 */
public class UserBean extends CoreUserBean{

    private String login;

    public UserBean() {
        super();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}
