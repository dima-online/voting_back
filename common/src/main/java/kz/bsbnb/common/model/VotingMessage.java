package kz.bsbnb.common.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.bsbnb.common.consts.Locale;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Olzhas.Pazyldayev on 14.12.2017.
 */

@Entity
@Table(name = "voting_message", schema = Constants.DB_SCHEMA_CORE)
public class VotingMessage implements IPersistable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "voting_id", foreignKey = @ForeignKey(name = "voting_message_voting_fk"))
    @JsonBackReference(value = "votingMessages")
    @JsonIgnore
    private Voting voting;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Locale locale = Locale.ru;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "subject")
    private String subject;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "description")
    private String description;

    public VotingMessage() {
    }

    public VotingMessage(Voting voting, Locale locale, String subject, String description) {
        this.voting = voting;
        this.locale = locale;
        this.subject = subject;
        this.description = description;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Voting getVoting() {
        return voting;
    }

    public void setVoting(Voting voting) {
        this.voting = voting;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

