package kz.bsbnb.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import kz.bsbnb.common.model.IPersistable;
import kz.bsbnb.common.model.UserRoles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * Created by Olzhas.Pazyldayev on 02.05.2017.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMapper implements IPersistable, UserDetails {
    private Long id;
    private String username;
    private Set<UserRoles> userRolesSet = new HashSet<>();
    private UserInfoMapper userInfo;
    private String status;
    private String executiveOfficeIin;

    public UserMapper() {
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

        for (UserRoles userRole : userRolesSet) {
            result.add(new SimpleGrantedAuthority(userRole.getRole().name()));
        }
        return result;
    }

    @Override
    public String getPassword() {
        return null;
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

    @JsonIgnore
    public Set<UserRoles> getUserRolesSet() {
        return userRolesSet;
    }

    public void setUserRolesSet(Set<UserRoles> userRolesSet) {
        this.userRolesSet = userRolesSet;
    }

    public UserInfoMapper getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoMapper userInfo) {
        this.userInfo = userInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExecutiveOfficeIin() {
        return executiveOfficeIin;
    }

    public void setExecutiveOfficeIin(String executiveOfficeIin) {
        this.executiveOfficeIin = executiveOfficeIin;
    }
}
