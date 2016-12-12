package kz.bsbnb.common.bean;

/**
 * Created by ruslan on 07.12.16.
 */
public class RepDecisionBean {

    private String answerText;
    private String comment;
    private Integer score;
    private Boolean isAnswer;
    public RepDecisionBean() {
    }

    public Boolean getAnswer() {
        return isAnswer;
    }

    public void setAnswer(Boolean answer) {
        isAnswer = answer;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
