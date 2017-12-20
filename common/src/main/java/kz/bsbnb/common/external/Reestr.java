package kz.bsbnb.common.external;


import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ruslan
 */
@Entity
@Table(name = "reestr", schema = "external")
@XmlRootElement
public class Reestr implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "name")
    private String name;
    @Size(max = 200)
    @Column(name = "firstname")
    private String firstname;
    @Size(max = 200)
    @Column(name = "surname")
    private String surname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "iin")
    private String iin;
    @Size(max = 100)
    @Column(name = "share_type")
    private String shareType;
    @Size(max = 100)
    @Column(name = "nin")
    private String nin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "share_count")
    private int shareCount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "share_percent")
    private double sharePercent;
    @Column(name = "all_share_count")
    private Integer allShareCount;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "email")
    private String email;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "phone")
    private String phone;
    @JoinColumn(name = "reestr_head_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ReestrHead reestrHeadId;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "voter_iin")
    private String voterIin;

    public Reestr() {
    }

    public Reestr(Long id) {
        this.id = id;
    }

    public Reestr(Long id, String name, String iin, int shareCount, double sharePercent) {
        this.id = id;
        this.name = name;
        this.iin = iin;
        this.shareCount = shareCount;
        this.sharePercent = sharePercent;
    }

    public String getVoterIin() {
        return voterIin;
    }

    public void setVoterIin(String voterIin) {
        this.voterIin = voterIin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public double getSharePercent() {
        return sharePercent;
    }

    public void setSharePercent(double sharePercent) {
        this.sharePercent = sharePercent;
    }

    public Integer getAllShareCount() {
        return allShareCount;
    }

    public void setAllShareCount(Integer allShareCount) {
        this.allShareCount = allShareCount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ReestrHead getReestrHeadId() {
        return reestrHeadId;
    }

    public void setReestrHeadId(ReestrHead reestrHeadId) {
        this.reestrHeadId = reestrHeadId;
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
        if (!(object instanceof Reestr)) {
            return false;
        }
        Reestr other = (Reestr) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "kz.bsbnb.common.external.Reestr[ id=" + id + " ]";
    }

}