package kz.bsbnb.common.model;

import kz.bsbnb.common.consts.ShareType;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by serik.mukashev on 13.12.2017.
 */
@Entity
@Table(name = "share", schema = Constants.DB_SCHEMA_CORE)
public class Share implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ShareType type;
    @Column(name = "amount")
    private Long amount;
    @ManyToOne
    @JoinColumn(name = "voter_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "share_voter_fk"))
    private Voter voter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShareType getType() {
        return type;
    }

    public void setType(ShareType type) {
        this.type = type;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Voter getVoter() {
        return voter;
    }

    public void setVoter(Voter voter) {
        this.voter = voter;
    }
}
