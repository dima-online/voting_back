package kz.bsbnb.common.bean;

import java.util.List;

/**
 * Created by ruslan on 08.12.16.
 */
public class RepQuestionBean {

    private Long id;
    private String question;
    private List<RepAnswerBean> repAnswerBeanList;
    private Integer maxCount;
    private Boolean privCanVote;

    public RepQuestionBean() {
    }

    public Boolean getPrivCanVote() {
        return privCanVote;
    }

    public void setPrivCanVote(Boolean privCanVote) {
        this.privCanVote = privCanVote;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<RepAnswerBean> getRepAnswerBeanList() {
        return repAnswerBeanList;
    }

    public void setRepAnswerBeanList(List<RepAnswerBean> repAnswerBeanList) {
        this.repAnswerBeanList = repAnswerBeanList;
    }
}
