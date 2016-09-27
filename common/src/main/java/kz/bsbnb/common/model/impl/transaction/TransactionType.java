package kz.bsbnb.common.model.impl.transaction;

import kz.bsbnb.common.model.IPersistable;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;

/**
 * Created by Olzhas.Pazyldayev on 16.09.2016.
 */
@Entity
@Table(
        name = "transaction_type",
        schema = Constants.DB_SCHEMA_CORE,
        uniqueConstraints = {@UniqueConstraint(name = "transaction_type_code_uk", columnNames = "code")}
)
public class TransactionType implements IPersistable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 30, nullable = false)
    private String code;

    @Column(length = 255, nullable = false)
    private String description;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
