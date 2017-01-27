package kz.bsbnb.common.bean;

/**
 * Created by ruslan on 11.12.16.
 */
public class TotalDecision {

    private String answerText;
    private Integer answerScore;
    private Integer answerCount;

    public TotalDecision() {
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Integer getAnswerScore() {
        return answerScore;
    }

    public void setAnswerScore(Integer answerScore) {
        this.answerScore = answerScore;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }
}