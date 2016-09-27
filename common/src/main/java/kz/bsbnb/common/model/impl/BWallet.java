package kz.bsbnb.common.model.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.bsbnb.common.model.IPersistable;
import kz.bsbnb.common.model.Status;
import kz.bsbnb.common.model.Wallet;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Olzhas.Pazyldayev on 16.09.2016.
 */
@Entity
@Table(name = "blockchain_wallet", schema = Constants.DB_SCHEMA_CORE)
public class BWallet implements Wallet<Long> {

    @Id
    private Long id;

    @Column(nullable = false)
    private BigDecimal balance;

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
}
