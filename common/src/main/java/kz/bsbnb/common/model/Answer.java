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
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ruslan
 */
@Entity
@Table(name = "answer", schema = Constants.DB_SCHEMA_CORE)
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "core.answer_id_seq", sequenceName = "core.answer_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "core.answer_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JsonIgnore
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Question question;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "answer", fetch = FetchType.LAZY)
    private Set<Decision> decisionSet;

    @BatchSize(size = 5)
    @OneToMany(mappedBy = "answer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "answerMessages")
    @OrderBy("locale ASC")
    private Set<AnswerMessage> messages = new HashSet<>();

    public Answer() {
    }

    public Answer(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @XmlTransient
    public Set<Decision> getDecisionSet() {
        return decisionSet;
    }

    public void setDecisionSet(Set<Decision> decisionSet) {
        this.decisionSet = decisionSet;
    }

    public Set<AnswerMessage> getMessages() {
        return messages;
    }

    public void setMessages(Set<AnswerMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + question.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Answer)) {
            return false;
        }
        Answer other = (Answer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "kz.bsbnb.common.model.Answer[ id=" + id + " ]";
    }

    @JsonIgnore
    public AnswerMessage getMessage(Locale locale) {
        try {
            return getMessages().parallelStream()
                    .filter(answerMessage -> answerMessage.getLocale().equals(locale)).findFirst().get();
        } catch (Exception e) {
        }
        return null;
    }

}
