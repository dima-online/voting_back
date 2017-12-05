/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.bsbnb.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author serik.mukashev
 */
@Entity
@Table(name = "user_roles", schema = Constants.DB_SCHEMA_CORE)
public class UserRoles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "core.user_roles_id_seq", sequenceName = "core.user_roles_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "core.user_roles_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "role_code")
    @Enumerated(EnumType.STRING)
    private Role role;
    @JsonIgnore
    @JoinColumn(name = "org_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Organisation orgId;
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User userId;
    @Column(name = "share_count")
    private Integer shareCount;
    @Column(name = "share_percent")
    private Double sharePercent;
    @Column(name = "cannot_vote")
    private Integer cannotVote;
    @Column(name = "share_date")
    private Date shareDate;

    public UserRoles() {
    }

    public Date getShareDate() {
        return shareDate;
    }

    public void setShareDate(Date shareDate) {
        this.shareDate = shareDate;
    }

    public UserRoles(Long id) {
        this.id = id;
    }

    public UserRoles(Long id, Role role) {
        this.id = id;
        this.role = role;
    }

    public Double getSharePercent() {
        return sharePercent;
    }

    public void setSharePercent(Double sharePercent) {
        this.sharePercent = sharePercent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Organisation getOrgId() {
        return orgId;
    }

    public void setOrgId(Organisation orgId) {
        this.orgId = orgId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Integer getShareCount() {return shareCount;}

    public void setShareCount(Integer shareCount) {this.shareCount = shareCount;}

    public Integer getCannotVote() {return cannotVote;}

    public void setCannotVote(Integer cannotVote) {this.cannotVote = cannotVote;}

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserRoles)) {
            return false;
        }
        UserRoles other = (UserRoles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "kz.bsbnb.common.model.UserRoles[ id=" + id + " ]";
    }
}
