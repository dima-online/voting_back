package kz.bsbnb.common.bean;

import java.util.Date;
import java.util.List;

/**
 * Created by ruslan on 08.12.16.
 */
public class RepVoterBean {
    private Long voterId;
    private Long userId;
    private Long questionId;
    private String userName;
    private Date decisionDate;
    private List<RepDecisionBean> decisionBeanList;

    public RepVoterBean() {
    }

    public Date getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(Date decisionDate) {
        this.decisionDate = decisionDate;
    }

    public Long getVoterId() {
        return voterId;
    }

    public void setVoterId(Long voterId) {
        this.voterId = voterId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
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
