package kz.bsbnb.common.bean;

/**
 * Created by serik.mukashev on 11.05.2017.
 */
public class SimpleDecisionBean {
    private String answerText;
    private Integer totalScore;
    private Long id;

    public SimpleDecisionBean(String answerText, Integer totalScore) {
        this.answerText = answerText;
        this.totalScore = totalScore;
    }

    public SimpleDecisionBean(String answerText, Integer totalScore, Long id) {
        this.answerText = answerText;
        this.totalScore = totalScore;
        this.id = id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
