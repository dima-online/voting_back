package kz.bsbnb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Olzhas.Pazyldayev on 05.12.2017.
 */
@Entity
@Table(name = "login_order", schema = Constants.DB_SCHEMA_CORE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginOrder {
    @Id
    @SequenceGenerator(name = "core.login_order_id_seq", sequenceName = "core.login_order_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "core.login_order_id_seq")
    private Long id;

    @Column(name = "signature")
    private String signature;

    @Column(name = "json_document")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String jsonDocument;

    @Column(name = "user_public_key")
    private String publicKey;

    @Column(name = "create_time")
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT_BOOTSTRAP)
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime; //"01-01-2003",

    @Transient
    User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getJsonDocument() {
        return jsonDocument;
    }

    public void setJsonDocument(String jsonDocument) {
        this.jsonDocument = jsonDocument;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
