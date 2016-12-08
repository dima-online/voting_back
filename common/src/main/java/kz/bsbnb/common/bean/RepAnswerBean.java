package kz.bsbnb.common.bean;

/**
 * Created by ruslan on 08.12.16.
 */
public class RepAnswerBean {
    private Long id;
    private String answerText;
    private Integer score;

    public RepAnswerBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
