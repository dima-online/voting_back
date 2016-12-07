package kz.bsbnb.common.bean;

import java.util.Date;

/**
 * Created by ruslan on 03.11.16.
 */
public class ConfirmBean {

    private Long userId;
    private Date dateConfirm;
    private String xmlBody;
    private String iin;

    public ConfirmBean() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getDateConfirm() {
        return dateConfirm;
    }

    public void setDateConfirm(Date dateConfirm) {
        this.dateConfirm = dateConfirm;
    }

    public String getXmlBody() {
        return xmlBody;
    }

    public void setXmlBody(String xmlBody) {
        this.xmlBody = xmlBody;
    }

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }
}
