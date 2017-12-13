package kz.bsbnb.common.bean;

import kz.bsbnb.common.model.QuestionMessage;

import java.util.List;
import java.util.Set;

/**
 * Created by ruslan on 10.12.16.
 */
public class RegQuestionBean {

    private Long id;
    private String questionType;
    private Integer num;
    private List<Long> filesId;
    private Integer maxCount;
    private Boolean privCanVote;
    private Set<QuestionMessage> messages;

    public RegQuestionBean() {
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

    public Set<QuestionMessage> getMessages() {
        return messages;
    }

    public void setMessages(Set<QuestionMessage> messages) {
        this.messages = messages;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public List<Long> getFilesId() {
        return filesId;
    }

    public void setFilesId(List<Long> filesId) {
        this.filesId = filesId;
    }
}
