package kz.bsbnb.common.bean;

import kz.bsbnb.common.model.Answer;
import kz.bsbnb.common.model.Files;

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
    private Set<DecisionBean> decisionSet;
    private Long votingId;
    private Set<Files> questionFileSet;
    public QuestionBean() {
    }

    public Set<Files> getQuestionFileSet() {
        return questionFileSet;
    }

    public void setQuestionFileSet(Set<Files> questionFileSet) {
        this.questionFileSet = questionFileSet;
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

    public Set<DecisionBean> getDecisionSet() {
        return decisionSet;
    }

    public void setDecisionSet(Set<DecisionBean> decisionSet) {
        this.decisionSet = decisionSet;
    }

    public Long getVotingId() {
        return votingId;
    }

    public void setVotingId(Long votingId) {
        this.votingId = votingId;
    }
}
