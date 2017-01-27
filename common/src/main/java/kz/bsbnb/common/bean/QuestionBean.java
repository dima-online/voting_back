package kz.bsbnb.common.bean;

import kz.bsbnb.common.model.Answer;
import kz.bsbnb.common.model.Files;

import java.util.List;
import java.util.Set;

public class QuestionBean {
    private Long id;
    private String question;
    private String questionType;
    private Integer num;
    private String decision;
    private List<TotalDecision> decisionOS;
    private List<Answer> answerSet;
    private List<DecisionBean> decisionSet;
    private Long votingId;
    private Set<Files> questionFileSet;
    private String decisionStatus;
    private Integer maxCount;
    private Boolean privCanVote;

    public QuestionBean() {
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

    public String getDecisionStatus() {
        return decisionStatus;
    }

    public void setDecisionStatus(String decisionStatus) {
        this.decisionStatus = decisionStatus;
    }

    public List<TotalDecision> getDecisionOS() {
        return decisionOS;
    }

    public void setDecisionOS(List<TotalDecision> decisionOS) {
        this.decisionOS = decisionOS;
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

    public List<Answer> getAnswerSet() {
        return answerSet;
    }

    public void setAnswerSet(List<Answer> answerSet) {
        this.answerSet = answerSet;
    }

    public List<DecisionBean> getDecisionSet() {
        return decisionSet;
    }

    public void setDecisionSet(List<DecisionBean> decisionSet) {
        this.decisionSet = decisionSet;
    }

    public Long getVotingId() {
        return votingId;
    }

    public void setVotingId(Long votingId) {
        this.votingId = votingId;
    }
}
