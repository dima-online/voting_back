package kz.bsbnb.common.bean;

import java.util.Date;

/**
 * Created by ruslan on 31.10.16.
 */
public class DecisionBean {
    private Long id;
    private String dateCreate;   //"dd-MM-yyyy"
    private Long score;
    private Long answerId;
    private Long questionId;
    private String comments;
    private Long userId;
    private Long proxyQuestionId;

    public DecisionBean() {
    }

      public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Long getScore() {
        if (score==null) {score = 0L;}
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProxyQuestionId() {
        return proxyQuestionId;
    }

    public void setProxyQuestionId(Long proxyQuestionId) {
        this.proxyQuestionId = proxyQuestionId;
    }
}
