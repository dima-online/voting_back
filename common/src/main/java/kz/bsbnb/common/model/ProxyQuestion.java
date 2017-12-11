package kz.bsbnb.common.model;

import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by serik.mukashev on 17.11.2017.
 */
@Entity
@Table(name = "proxy_question",  schema = Constants.DB_SCHEMA_CORE)
public class ProxyQuestion implements Serializable{
    @Id
    @SequenceGenerator(name = "core.proxy_question_id_seq", sequenceName = "core.proxy_question_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "core.proxy_question_id_seq")
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name="question_id")
    private Question question;
    @ManyToOne
    @JoinColumn(name="parent_user_id")
    private User parentUser;
    @ManyToOne
    @JoinColumn(name="executive_voter_id")
    private Voter executiveVoter;
    @ManyToOne
    @JoinColumn(name="proxy_card_id")
    private ProxyCard proxyCard;


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

    public User getParentUser() {
        return parentUser;
    }

    public void setParentUser(User parentUser) {
        this.parentUser = parentUser;
    }

    public Voter getExecutiveVoter() {
        return executiveVoter;
    }

    public void setExecutiveVoter(Voter executiveVoter) {
        this.executiveVoter = executiveVoter;
    }

    public ProxyCard getProxyCard() {
        return proxyCard;
    }

    public void setProxyCard(ProxyCard proxyCard) {
        this.proxyCard = proxyCard;
    }
}
