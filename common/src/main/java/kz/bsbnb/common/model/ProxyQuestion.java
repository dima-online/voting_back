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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name="question_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "proxy_question_question_fk"))
    private Question question;
    @ManyToOne
    @JoinColumn(name="parent_user_id",  referencedColumnName = "id", foreignKey = @ForeignKey(name = "proxy_question_parent_voter_fk"))
    private Voter parentVoter;
    @ManyToOne
    @JoinColumn(name="executive_voter_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "proxy_question_executive_voter_fk"))
    private Voter executiveVoter;
    @ManyToOne
    @JoinColumn(name="proxy_card_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "proxy_question_proxy_card_fk"))
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

    public Voter getParentVoter() {
        return parentVoter;
    }

    public void setParentVoter(Voter parentVoter) {
        this.parentVoter = parentVoter;
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
