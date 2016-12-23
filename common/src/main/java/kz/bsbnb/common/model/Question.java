/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.bsbnb.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Set;

/**
 * @author ruslan
 */
@Entity
@Table(name = "question", schema = Constants.DB_SCHEMA_CORE)
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "core.question_id_seq", sequenceName = "core.question_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "core.question_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "question")
    private String question;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "question_type")
    private String questionType;
    @Column(name = "max_count")
    private Integer maxCount;
    @Column(name = "num")
    private Integer num;
    @Size(max = 2000)
    @Column(name = "decision")
    private String decision;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "questionId", fetch = FetchType.EAGER)
    private Set<Answer> answerSet;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "questionId", fetch = FetchType.EAGER)
    private Set<Decision> decisionSet;
    @JsonIgnore
    @JoinColumn(name = "voting_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Voting votingId;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "questionId", fetch = FetchType.EAGER)
    private Set<QuestionFile> questionFileSet;

    public Question() {
    }

    public Question(Long id) {
        this.id = id;
    }

    public Question(Long id, String question) {
        this.id = id;
        this.question = question;
    }

    @XmlTransient
    public Set<QuestionFile> getQuestionFileSet() {
        return questionFileSet;
    }

    public void setQuestionFileSet(Set<QuestionFile> questionFileSet) {
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

    @XmlTransient
    public Set<Answer> getAnswerSet() {
        return answerSet;
    }

    public void setAnswerSet(Set<Answer> answerSet) {
        this.answerSet = answerSet;
    }

    @XmlTransient
    public Set<Decision> getDecisionSet() { return decisionSet;}

    public void setDecisionSet(Set<Decision> decisionSet) { this.decisionSet = decisionSet;}

    public Voting getVotingId() {
        return votingId;
    }

    public void setVotingId(Voting votingId) {
        this.votingId = votingId;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Question)) {
            return false;
        }
        Question other = (Question) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "kz.bsbnb.common.model.Question[ id=" + id + " ]";
    }

}
