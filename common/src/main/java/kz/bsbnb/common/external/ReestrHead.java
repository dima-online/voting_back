package kz.bsbnb.common.external;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ruslan
 */
@Entity
@Table(name = "reestr_head", schema = "external")
public class ReestrHead implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "iin")
    private String iin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "org_name")
    private String orgName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_create")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreate;
    @Size(max = 20)
    @Column(name = "status")
    private String status;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "reestrHeadId")
    private Set<Reestr> reestrSet;

    public ReestrHead() {
    }

    public ReestrHead(Long id) {
        this.id = id;
    }

    public ReestrHead(Long id, String iin, String orgName, Date dateCreate) {
        this.id = id;
        this.iin = iin;
        this.orgName = orgName;
        this.dateCreate = dateCreate;
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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public Set<Reestr> getReestrSet() {
        return reestrSet;
    }

    public void setReestrSet(Set<Reestr> reestrSet) {
        this.reestrSet = reestrSet;
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
        if (!(object instanceof ReestrHead)) {
            return false;
        }
        ReestrHead other = (ReestrHead) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "kz.bsbnb.common.external.ReestrHead[ id=" + id + " ]";
    }

}
