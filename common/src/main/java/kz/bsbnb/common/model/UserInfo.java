/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.bsbnb.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import kz.bsbnb.common.consts.DocType;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author serik.mukashev
 */
@Entity
@Table(name = "user_info", schema = Constants.DB_SCHEMA_CORE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 200)
    @Column(name = "last_name")
    private String lastName;
    @Size(max = 200)
    @Column(name = "first_name")
    private String firstName;
    @Size(max = 200)
    @Column(name = "middle_name")
    private String middleName;
    @Size(max = 12)
    @Column(name = "idn")
    private String idn;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "phone")
    private String phone;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @JsonIgnore
    @OneToOne(mappedBy = "userInfo", fetch = FetchType.LAZY)
    private User user;
    @Column(name = "is_org")
    private Boolean isOrg;
    @Column(name = "sms_notification")
    private Boolean smsNotification;
    @Column(name = "email_notification")
    private Boolean emailNotification;
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = Constants.DATE_FORMAT)
    private Date dateOfBirth;
    @Column(name = "document_number")
    private String documentNumber;
    @Column(name = "document_given_agency")
    private String documentGivenAgency;
    @Column(name = "document_given_date")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = Constants.DATE_FORMAT)
    private Date documentGivenDate;
    @Column(name = "document_expire_date")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = Constants.DATE_FORMAT)
    private Date documentExpireDate;
    @Column(name = "document_type")
    @Enumerated(EnumType.STRING)
    private DocType documentType;

    //!!!Delete later!!!
    @Size(max = 12)
    @Column(name = "voter_iin")
    private String voterIin;
    //!!!Delete later!!!

    public UserInfo() {
    }

    public String getVoterIin() {
        return voterIin;
    }

    public void setVoterIin(String voterIin) {
        this.voterIin = voterIin;
    }

    public UserInfo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

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


    public Boolean getSmsNotification() {
        return smsNotification;
    }

    public void setSmsNotification(Boolean smsNotification) {
        this.smsNotification = smsNotification;
    }

    public Boolean getEmailNotification() {
        return emailNotification;
    }

    public void setEmailNotification(Boolean emailNotification) {
        this.emailNotification = emailNotification;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getOrg() {return isOrg;}

    public void setOrg(Boolean org) {isOrg = org;}

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentGivenAgency() {
        return documentGivenAgency;
    }

    public void setDocumentGivenAgency(String documentGivenAgency) {
        this.documentGivenAgency = documentGivenAgency;
    }

    public Date getDocumentGivenDate() {
        return documentGivenDate;
    }

    public void setDocumentGivenDate(Date documentGivenDate) {
        this.documentGivenDate = documentGivenDate;
    }



    public Date getDocumentExpireDate() {
        return documentExpireDate;
    }

    public void setDocumentExpireDate(Date documentExpireDate) {
        this.documentExpireDate = documentExpireDate;
    }

    public DocType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocType documentType) {
        this.documentType = documentType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserInfo)) return false;

        UserInfo userInfo = (UserInfo) o;

        if (!lastName.equals(userInfo.lastName)) return false;
        if (!firstName.equals(userInfo.firstName)) return false;
        if (!middleName.equals(userInfo.middleName)) return false;
        if (!phone.equals(userInfo.phone)) return false;
        if (!email.equals(userInfo.email)) return false;
        return voterIin != null ? voterIin.equals(userInfo.voterIin) : userInfo.voterIin == null;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", smsNotification=" + smsNotification +
                ", emailNotification=" + emailNotification +
                '}';
    }
}
