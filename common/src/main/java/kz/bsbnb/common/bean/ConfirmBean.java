package kz.bsbnb.common.bean;

import java.util.Date;

/**
 * Created by ruslan on 03.11.16.
 */
public class ConfirmBean {

    private Long id;
    private Date dateConfirm;
    private String publicKey;
    private String signature;
    private UserBean user;

    public ConfirmBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateConfirm() {
        return dateConfirm;
    }

    public void setDateConfirm(Date dateConfirm) {
        this.dateConfirm = dateConfirm;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
