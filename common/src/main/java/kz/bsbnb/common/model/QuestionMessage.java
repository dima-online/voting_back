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
@Table(name = "question_message", schema = Constants.DB_SCHEMA_CORE)
public class QuestionMessage implements IPersistable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "question_message_question_fk"))
    @JsonBackReference(value = "questionMessages")
    private Question question;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Locale locale = Locale.ru;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "title")
    private String title;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "text")
    private String text;

    public QuestionMessage() {
    }

    public QuestionMessage(Question question, Locale locale, String title, String text) {
        this.question = question;
        this.locale = locale;
        this.title = title;
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

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

