package kz.bsbnb.processor.impl;

import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.UserRoles;
import kz.bsbnb.processor.UserProcessor;
import kz.bsbnb.repository.IUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Olzhas.Pazyldayev on 20.09.2016.
 */
@Service
public class UserProcessorImpl implements UserProcessor {

    @Autowired
    private IUserRoleRepository userRoleRepository;

    public void mergeUser(User user) {
        Set<UserRoles> roles = new HashSet<>();
        user.getAuthorities().forEach(userRole1 -> {
            UserRoles userRole = userRoleRepository.findByRole(Role.getRole(userRole1.getAuthority()));
            roles.add(userRole);
        });
        user.setUserRolesSet(roles);

    }
}
