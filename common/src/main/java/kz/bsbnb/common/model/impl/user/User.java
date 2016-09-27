package kz.bsbnb.common.model.impl.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kz.bsbnb.common.model.IPersistable;
import kz.bsbnb.common.model.Status;
import kz.bsbnb.common.util.Constants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

/**
 * Created by kanattulbassiyev on 8/7/16.
 * Updated by Olzhas.Pazyldayev on 12.08.2016.
 * Updated by Timur.Abdykarimov on 15.08.2016.
 */
@Entity
@Table(name = "user", schema = Constants.DB_SCHEMA_CORE
        //,uniqueConstraints = {@UniqueConstraint(name = "user_iin_uk", columnNames = "iin")}
)
public class User implements IPersistable, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String iin;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(schema = Constants.DB_SCHEMA_CORE, name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            foreignKey = @ForeignKey(name = "user_id_role_fk"),
            inverseForeignKey = @ForeignKey(name = "role_id_user_fk"))
    private Set<UserRole> userRoles = new HashSet<>();

    @JoinColumn(name = "user_info_id", foreignKey = @ForeignKey(name = "user_user_info_fk"))
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserInfo userInfo;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)

    private Status status = Status.ACTIVE;

    public User() {
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(Long id, String username, String password, Status status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> result = new ArrayList<>();

        for (UserRole userRole : userRoles) {
            result.add(new SimpleGrantedAuthority(userRole.getRole().name()));
        }
        return result;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIin() {
        return iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
