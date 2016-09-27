package kz.bsbnb.common.model.impl.user;

import kz.bsbnb.common.model.IPersistable;
import kz.bsbnb.common.model.Role;
import kz.bsbnb.common.util.Constants;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by Timur.Abdykarimov on 14.08.2016.
 */
@Entity
@Table(name = "user_role", schema = Constants.DB_SCHEMA_CORE)
public class UserRole implements IPersistable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "userRoles")
    private Set<User> user = new HashSet<>();

    public UserRole() {

    }

    public UserRole(Long id, Role role) {
        this.id = id;
        this.role = role;
    }

    @Override
    public Long getId() {
        return this.getId();
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
