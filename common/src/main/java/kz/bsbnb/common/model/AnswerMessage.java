package kz.bsbnb.common.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import kz.bsbnb.common.consts.Locale;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Olzhas.Pazyldayev on 14.12.2017.
 */

@Entity
@Table(name = "answer_message", schema = Constants.DB_SCHEMA_CORE)
public class AnswerMessage implements IPersistable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "answer_id", foreignKey = @ForeignKey(name = "answer_message_answer_fk"))
    @JsonBackReference(value = "answerMessages")
    private Answer answer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Locale locale = Locale.ru;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "text")
    private String text;

    @Column(name = "description", length = 100_000)
    private String description;

    public AnswerMessage() {
    }

    public AnswerMessage(Locale locale, String text) {
        this.locale = locale;
        this.text = text;
    }

    public AnswerMessage(Answer answer, Locale locale, String text) {
        this.answer = answer;
        this.locale = locale;
        this.text = text;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

