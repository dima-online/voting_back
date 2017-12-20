/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.bsbnb.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import kz.bsbnb.common.util.Constants;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import org.springframework.security.core.GrantedAuthority;


/**
 *
 * @author ruslan
 */
@Entity
@Table(name = "user", schema = Constants.DB_SCHEMA_CORE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "iin",unique = true)
    private String iin;
    @Size(max = 255)
    @Column(name = "username",unique = true)
    private String username;
    @Size(max = 255)
    @Column(name = "password")
    private String password;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @JsonIgnore
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "user", fetch = FetchType.LAZY)
    private Set<UserRoles> userRolesSet;
//    @JsonIgnore
    @JoinColumn(name = "user_info_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "user_user_info_fk"))
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private UserInfo userInfo;
    @Size(max = 255)
    @Column(name="executiveIin")
    private String executiveOfficeIin;
    @Column(name = "account_number")
    private String accountNumber;

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @XmlTransient
    public Set<UserRoles> getUserRolesSet() {
        return userRolesSet;
    }

    public void setUserRolesSet(Set<UserRoles> userRolesSet) {
        this.userRolesSet = userRolesSet;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getExecutiveOfficeIin() {
        return executiveOfficeIin;
    }

    public void setExecutiveOfficeIin(String executiveOfficeIin) {
        this.executiveOfficeIin = executiveOfficeIin;
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
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!iin.equals(user.iin)) return false;
        if (!username.equals(user.username)) return false;
        if (!status.equals(user.status)) return false;
        return userInfo.equals(user.userInfo);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", iin='" + iin + '\'' +
                ", username='" + username + '\'' +
                ", userInfo=" + userInfo +
                '}';
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> result = new ArrayList<>();
        if(userRolesSet == null || userRolesSet.size() == 0) return null;
        for (UserRoles userRole : userRolesSet) {
            result.add(new SimpleGrantedAuthority(userRole.getRole().name()));
        }
        return result;
    }
}
