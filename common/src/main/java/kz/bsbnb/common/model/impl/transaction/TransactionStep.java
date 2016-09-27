package kz.bsbnb.common.model.impl.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.bsbnb.common.model.IPersistable;
import kz.bsbnb.common.model.Status;
import kz.bsbnb.common.model.Step;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Olzhas.Pazyldayev on 21.09.2016.
 */
@Entity
@Table(name = "transaction_step", schema = Constants.DB_SCHEMA_CORE)
public class TransactionStep implements IPersistable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id", foreignKey = @ForeignKey(name = "transaction_step_transaction_fk"))
    private Transaction transaction;

    @Column(name = "start_time")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime; //"01-01-2003",

    @Column(name = "end_time")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime; //"01-01-2003",

    @Enumerated(EnumType.STRING)
    @Column(name = "step", nullable = false)
    private Step step = Step.NEW;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.ACTIVE;

    @Column(length = 1000)
    private String comment;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
