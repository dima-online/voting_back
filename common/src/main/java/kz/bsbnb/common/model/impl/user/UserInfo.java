package kz.bsbnb.common.model.impl.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import kz.bsbnb.common.model.IPersistable;
import kz.bsbnb.common.model.Status;
import kz.bsbnb.common.util.Constants;
import kz.bsbnb.util.JsonDateSerializer;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Olzhas.Pazyldayev on 15.09.2016.
 */
@Entity
@Table(name = "user_info", schema = Constants.DB_SCHEMA_CORE)
public class UserInfo implements IPersistable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "last_name", length = 200, nullable = false)
    private String lastName; //"Иванов",

    @Column(name = "first_name", length = 200, nullable = false)
    private String firstName; //"Петр",

    @Column(name = "middle_name", length = 200, nullable = true)
    private String middleName; //"Петрович",

    @Column(name = "idn", length = 12)
    private String idn; //"123456789012",

    @JoinColumn(name = "user_doc_type_id", foreignKey = @ForeignKey(name = "user_info_user_doc_type_fk"))
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private DocType typeDoc; //02

    @Column(name = "num_doc", length = 200)
    private String numDoc; //"006545712",

    @Column(name = "date_doc")
    @JsonFormat(pattern = "dd-MM-yyyy")
    // @JsonSerialize(using = JsonDateSerializer.class)
    @Temporal(TemporalType.DATE)
    private Date dateDoc; //"01-01-2003",

    @Column(name = "issuer_doc", length = 500)
    private String issuerDoc; //"МВД РК",

    @Column(name = "expire_doc")
    @JsonFormat(pattern = "dd-MM-yyyy")
    //@JsonSerialize(using = JsonDateSerializer.class)
    @Temporal(TemporalType.DATE)
    private Date expireDoc; //"01-01-2023",

    @Column(name = "citizenship")
    private String citizenship; //"KZ",

    @JoinColumn(name = "address_id", foreignKey = @ForeignKey(name = "user_info_address_fk"))
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Address address;

    @Column(name = "phone")
    private String phone; //"887-54-8545",
    @Column(name = "email")
    private String email; //"ritchie@mail.ru",

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


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getIdn() {
        return idn;
    }

    public void setIdn(String idn) {
        this.idn = idn;
    }

    public DocType getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(DocType typeDoc) {
        this.typeDoc = typeDoc;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public Date getDateDoc() {
        return dateDoc;
    }

    public void setDateDoc(Date dateDoc) {
        this.dateDoc = dateDoc;
    }

    public String getIssuerDoc() {
        return issuerDoc;
    }

    public void setIssuerDoc(String issuerDoc) {
        this.issuerDoc = issuerDoc;
    }

    public Date getExpireDoc() {
        return expireDoc;
    }

    public void setExpireDoc(Date expireDoc) {
        this.expireDoc = expireDoc;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
