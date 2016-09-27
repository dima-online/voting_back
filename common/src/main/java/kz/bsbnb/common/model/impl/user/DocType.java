package kz.bsbnb.common.model.impl.user;

import kz.bsbnb.common.model.IPersistable;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;

/**
 * Created by Olzhas.Pazyldayev on 15.09.2016.
 */
@Entity
@Table(
        name = "user_doc_type",
        schema = Constants.DB_SCHEMA_CORE,
        uniqueConstraints = {@UniqueConstraint(name = "user_doc_type_code_uk", columnNames = "code")}
)
public class DocType implements IPersistable {
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
