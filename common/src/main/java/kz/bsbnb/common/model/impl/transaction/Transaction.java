package kz.bsbnb.common.model.impl.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.bsbnb.common.model.IPersistable;
import kz.bsbnb.common.model.Status;
import kz.bsbnb.common.model.impl.BWallet;
import kz.bsbnb.common.model.impl.EWallet;
import kz.bsbnb.common.model.impl.user.User;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * Created by Olzhas.Pazyldayev on 15.09.2016.
 */
@Entity
@Table(name = "transaction", schema = Constants.DB_SCHEMA_CORE)
public class Transaction implements IPersistable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "transaction_user_fk"))
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ekzt_wallet_sender_id", foreignKey = @ForeignKey(name = "transaction_ekzt_wallet_sender_fk"))
    private EWallet eWalletSender;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ekzt_wallet_receiver_id", foreignKey = @ForeignKey(name = "transaction_ekzt_wallet_receiver_fk"))
    private EWallet eWalletReceiver;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "blockchain_wallet_sender_id", foreignKey = @ForeignKey(name = "transaction_blockchain_wallet_sender_fk"))
    private BWallet bWalletSender;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "blockchain_wallet_receiver_id", foreignKey = @ForeignKey(name = "transaction_blockchain_wallet_receiver_fk"))
    private BWallet bWalletReceiver;

    @Column(name = "start_time")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime; //"01-01-2003",

    @Column(name = "end_time")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime; //"01-01-2003",

    @Column(name = "amount_ekzt", nullable = false)
    private BigDecimal amountEkzt;

    @Column(name = "amount_blockchain", nullable = true)
    private BigDecimal amountBlockChain;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_type_id", foreignKey = @ForeignKey(name = "transaction_transaction_type_fk"))
    private TransactionType transactionType;

    @OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<TransactionStep> steps;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.ACTIVE;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EWallet geteWalletSender() {
        return eWalletSender;
    }

    public void seteWalletSender(EWallet eWalletSender) {
        this.eWalletSender = eWalletSender;
    }

    public EWallet geteWalletReceiver() {
        return eWalletReceiver;
    }

    public void seteWalletReceiver(EWallet eWalletReceiver) {
        this.eWalletReceiver = eWalletReceiver;
    }

    public BWallet getbWalletSender() {
        return bWalletSender;
    }

    public void setbWalletSender(BWallet bWalletSender) {
        this.bWalletSender = bWalletSender;
    }

    public BWallet getbWalletReceiver() {
        return bWalletReceiver;
    }

    public void setbWalletReceiver(BWallet bWalletReceiver) {
        this.bWalletReceiver = bWalletReceiver;
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

    public BigDecimal getAmountEkzt() {
        return amountEkzt;
    }

    public void setAmountEkzt(BigDecimal amountEkzt) {
        this.amountEkzt = amountEkzt;
    }

    public BigDecimal getAmountBlockChain() {
        return amountBlockChain;
    }

    public void setAmountBlockChain(BigDecimal amountBlockChain) {
        this.amountBlockChain = amountBlockChain;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Set<TransactionStep> getSteps() {
        return steps;
    }

    public void setSteps(Set<TransactionStep> steps) {
        this.steps = steps;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
