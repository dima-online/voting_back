package kz.bsbnb.common.bean;

/**
 * Created by ruslan on 11.12.16.
 */
public class TotalDecision {

    private String answerText;
    private Long answerScore;
    private Long answerCount;

    public TotalDecision() {
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Long getAnswerScore() {
        return answerScore;
    }

    public void setAnswerScore(Long answerScore) {
        this.answerScore = answerScore;
    }

    public Long getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Long answerCount) {
        this.answerCount = answerCount;
    }
}