package kz.bsbnb.common.bean;

import java.util.List;

/**
 * Created by ruslan on 08.12.16.
 */
public class RepVoterBean {
    private Long id;
    private Long userId;
    private String userName;
    private List<RepDecisionBean> decisionBeanList;

    public RepVoterBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<RepDecisionBean> getDecisionBeanList() {
        return decisionBeanList;
    }

    public void setDecisionBeanList(List<RepDecisionBean> decisionBeanList) {
        this.decisionBeanList = decisionBeanList;
    }
}
