package kz.bsbnb.common.model;

import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ruslan
 */
@Entity
@Table(name = "decision", schema = Constants.DB_SCHEMA_CORE)
@XmlRootElement
public class Decision implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_create")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreate;
    @Column(name = "score")
    private Long score;
    @JoinColumn(name = "answer_id", referencedColumnName = "id", nullable = true, foreignKey = @ForeignKey(name = "decision_answer_fk"))
    @ManyToOne(fetch = FetchType.EAGER)
    private Answer answer;
    @JoinColumn(name = "voter_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "decision_voter_fk"))
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Voter voter;
    @Column(name = "comments")
    private String comments;
    @Column(name = "status")
    private String status;
    @Column(name = "cancel_reason")
    private String cancelReason;
    @Column(name = "proxy_question_id")
    private ProxyQuestion proxyQuestion;

    public Decision() {
    }

    public Decision(Long id) {
        this.id = id;
    }

    public Decision(Long id, Date dateCreate) {
        this.id = id;
        this.dateCreate = dateCreate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Question getQuestion() {
        return answer.getQuestion();
    }

    public Voter getVoter() {
        return voter;
    }

    public void setVoter(Voter voter) {
        this.voter = voter;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public ProxyQuestion getProxyQuestion() {
        return proxyQuestion;
    }

    public void setProxyQuestion(ProxyQuestion proxyQuestion) {
        this.proxyQuestion = proxyQuestion;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + dateCreate.hashCode();
        result = 31 * result + score.hashCode();
        result = 31 * result + answer.getId().hashCode();
        result = 31 * result + answer.getQuestion().getId().hashCode();
        result = 31 * result + voter.getUser().getIin().hashCode();
        if (proxyQuestion != null)
            result = 31 * result + proxyQuestion.getParentVoter().getUser().getIin().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Decision)) {
            return false;
        }
        Decision other = (Decision) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "kz.bsbnb.common.model.Decision[ id=" + id + " ]";
    }

}
