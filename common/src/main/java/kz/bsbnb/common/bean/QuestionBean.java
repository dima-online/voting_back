package kz.bsbnb.common.bean;

import kz.bsbnb.common.model.Answer;
import kz.bsbnb.common.model.Decision;

import java.util.Set;

/**
 * Created by ruslan on 31.10.16.
 */
public class QuestionBean {
    private Long id;
    private String question;
    private String questionType;
    private Integer num;
    private String decision;
    private Set<Answer> answerSet;
    private Set<Decision> decisionSet;
    private Long votingId;

    public QuestionBean() {
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

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public Set<Answer> getAnswerSet() {
        return answerSet;
    }

    public void setAnswerSet(Set<Answer> answerSet) {
        this.answerSet = answerSet;
    }

    public Set<Decision> getDecisionSet() {
        return decisionSet;
    }

    public void setDecisionSet(Set<Decision> decisionSet) {
        this.decisionSet = decisionSet;
    }

    public Long getVotingId() {
        return votingId;
    }

    public void setVotingId(Long votingId) {
        this.votingId = votingId;
    }
}