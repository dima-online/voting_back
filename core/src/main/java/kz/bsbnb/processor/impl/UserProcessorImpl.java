package kz.bsbnb.processor.impl;

import kz.bsbnb.common.model.Role;
import kz.bsbnb.common.model.impl.user.User;
import kz.bsbnb.common.model.impl.user.UserRole;
import kz.bsbnb.processor.UserProcessor;
import kz.bsbnb.repository.IUserDocTypeRepository;
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
    @Autowired
    private IUserDocTypeRepository userDocTypeRepository;

    public void mergeUser(User user) {
        Set<UserRole> roles = new HashSet<>();
        user.getAuthorities().forEach(userRole1 -> {
            UserRole userRole = userRoleRepository.findByRole(Role.getRole(userRole1.getAuthority()));
            roles.add(userRole);
        });
        user.setUserRoles(roles);
        user.getUserInfo().setTypeDoc(userDocTypeRepository.findOne(user.getUserInfo().getTypeDoc().getId()));
    }
}
