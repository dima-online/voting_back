package kz.bsbnb.common.bean;

import java.io.Serializable;

/**
 * Created by serik.mukashev on 28.12.2017.
 */
public class ProxyQuestionBean implements Serializable {
    private Long id;
    private Long executiveVoterId;
    private Long questionId;
    private Long parentVoterId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExecutiveVoterId() {
        return executiveVoterId;
    }

    public void setExecutiveVoterId(Long executiveVoterId) {
        this.executiveVoterId = executiveVoterId;
    }

    public Long getParentVoterId() {
        return parentVoterId;
    }

    public void setParentVoterId(Long parentVoterId) {
        this.parentVoterId = parentVoterId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
}
