package kz.bsbnb.common.model.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.bsbnb.common.model.Status;
import kz.bsbnb.common.model.Wallet;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Olzhas.Pazyldayev on 01.09.2016.
 */
@Entity
@Table(name = "ekzt_wallet", schema = Constants.DB_SCHEMA_CORE)
public class EWallet implements Wallet<Long> {

    @Id
    private Long id;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "blocked_balance")
    private BigDecimal blockedBalance;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.ACTIVE;

    @Column(name = "description")
    private String description;

    @Column(name = "ip")
    private String ip;

    //default transaction limit
    @Column(name = "code_word")
    private String codeWord;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getBlockedBalance() {
        return blockedBalance;
    }

    public void setBlockedBalance(BigDecimal blockedBalance) {
        this.blockedBalance = blockedBalance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodeWord() {
        return codeWord;
    }

    public void setCodeWord(String codeWord) {
        this.codeWord = codeWord;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
