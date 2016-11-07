package kz.bsbnb.common.bean;

import java.util.Date;

/**
 * Created by ruslan on 31.10.16.
 */
public class DecisionBean {
    private Long id;
    private Date dateCreate;
    private Integer score;
    private Long answerId;
    private Long questionId;
    private String comments;
    private Long userId;

    public DecisionBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Integer getScore() {
        if (score==null) {score = 0;}
        return score;
    }

    public void setScore(Integer score) {
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
}
