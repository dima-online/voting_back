package kz.bsbnb.common.bean;

import java.util.List;

/**
 * Created by ruslan on 10.12.16.
 */
public class RegQuestionBean {

    private Long id;
    private String question;
    private String questionType;
    private Integer num;
    private List<Long> filesId;

    public RegQuestionBean() {
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
