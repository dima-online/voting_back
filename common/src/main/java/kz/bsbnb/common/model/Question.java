/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.bsbnb.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kz.bsbnb.common.consts.Locale;
import kz.bsbnb.common.util.Constants;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ruslan
 */
@Entity
@Table(name = "question", schema = Constants.DB_SCHEMA_CORE)
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @BatchSize(size = 5)
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "questionMessages")
    @OrderBy("locale ASC")
    private Set<QuestionMessage> messages = new HashSet<>();

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
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "question", fetch = FetchType.EAGER)
    private Set<Answer> answerSet;
    @JsonIgnore
    @JoinColumn(name = "voting_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "question_voting_fk"))
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Voting voting;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "question", fetch = FetchType.EAGER)
    private Set<QuestionFile> questionFileSet;
    @Column(name = "priv_can_vote")
    private Boolean privCanVote;

    public Question() {
    }

    public Boolean getPrivCanVote() {
        return privCanVote;
    }

    public void setPrivCanVote(Boolean privCanVote) {
        this.privCanVote = privCanVote;
    }

    public Question(Long id) {
        this.id = id;
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

    public Set<QuestionMessage> getMessages() {
        return messages;
    }

    public void setMessages(Set<QuestionMessage> messages) {
        this.messages = messages;
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

    public Voting getVoting() {
        return voting;
    }

    public void setVoting(Voting voting) {
        this.voting = voting;
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

    @JsonIgnore
    public QuestionMessage getMessage(Locale locale) {
        try {
            return getMessages().parallelStream()
                    .filter(questionMessage -> questionMessage.getLocale().equals(locale)).findFirst().get();
        } catch (Exception e) {
        }
        return null;
    }


}
