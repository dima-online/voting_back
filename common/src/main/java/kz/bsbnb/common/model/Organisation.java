/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.bsbnb.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author ruslan
 */
@Entity
@Table(name = "organisation", schema = Constants.DB_SCHEMA_CORE)
public class Organisation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 200)
    @Column(name = "organisation_name")
    private String organisationName;
    @Size(max = 50)
    @Column(name = "organisation_num")
    private String organisationNum;
    @Size(max = 50)
    @Column(name = "external_id")
    private String externalId;
    @Size(max = 20)
    @Column(name = "status")
    private String status;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organisation", fetch = FetchType.LAZY)
    private Set<Voting> votingSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organisation", fetch = FetchType.EAGER)
    private Set<UserRoles> userRolesSet;
    @JsonIgnore
    @OneToMany(mappedBy = "organisation", fetch = FetchType.LAZY)
    private Set<Message> messageSet;
    @OneToMany(mappedBy = "organisation")
    private Set<Share> shares;
    @Size(max = 200)
    @Column(name = "executive_name",nullable = true)
    private String executiveName;
    @Size(max = 5_000_000)
    @Column(name = "logo")
    private String logo;
    @Column(name = "result_link")
    private String resultLink;
    @Column(name = "address")
    private String address;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;

    public Organisation() {
    }

    public Organisation(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getOrganisationNum() {
        return organisationNum;
    }

    public void setOrganisationNum(String organisationNum) {
        this.organisationNum = organisationNum;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getResultLink() {
        return resultLink;
    }

    public void setResultLink(String resultLink) {
        this.resultLink = resultLink;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExecutiveName() {
        return executiveName;
    }

    public void setExecutiveName(String executiveName) {
        this.executiveName = executiveName;
    }

    @XmlTransient
    public Set<Voting> getVotingSet() {
        return votingSet;
    }

    public void setVotingSet(Set<Voting> votingSet) {
        this.votingSet = votingSet;
    }

    @JsonIgnore
    @XmlTransient
    public Set<UserRoles> getUserRolesSet() {
        return userRolesSet;
    }

    public void setUserRolesSet(Set<UserRoles> userRolesSet) {
        this.userRolesSet = userRolesSet;
    }

    @XmlTransient
    public Set<Message> getMessageSet() {
        return messageSet;
    }

    public void setMessageSet(Set<Message> messageSet) {
        this.messageSet = messageSet;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
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

    public Set<Share> getShares() {
        return shares;
    }

    public void setShares(Set<Share> shares) {
        this.shares = shares;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Organisation)) {
            return false;
        }
        Organisation other = (Organisation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "kz.bsbnb.common.model.Organisation[ id=" + id + " ]";
    }
    
}
